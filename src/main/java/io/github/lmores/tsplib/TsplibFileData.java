package io.github.lmores.tsplib;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.github.lmores.tsplib.TsplibFileFormat.DisplayDataType;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeDataFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;
import io.github.lmores.tsplib.TsplibFileFormat.TsplibFileFormatException;
import io.github.lmores.tsplib.TsplibFileFormat.NodeCoordType;
import io.github.lmores.tsplib.TsplibFileFormat.ProblemType;

/**
 * Used internally to store the data found when reading an instance file in the
 * the TSPLIB format.
 *
 * This class is completely implementation-dependent and not available externally.
 *
 * @param name              the name of the instance
 * @param type              the type of problem
 * @param comment           the comment associated with the instance
 * @param dimension         the number of nodes
 * @param depots            declared depots
 * @param fixedEdges        list of fixed edges
 * @param nodeCoords        node coordinates
 * @param edgeWeights       the edge weights
 * @param displayCoords     node coordinates for graph representation only
 * @param edgeWeightType    how edge weights are computed
 * @param edgeWeightFormat  how edge weight are provided
 * @param edgeFormatData    how edges are provided
 * @param nodeCoordType     how node coordinates are provided
 * @param dataDisplayType   how nodes should be displayed
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public record TsplibFileData(
  String name,
  ProblemType type,
  String comment,
  int dimension,
  int[] depots,
  int[][] fixedEdges,
  int[] edgeWeights,
  double[][] nodeCoords,
  double[][] displayCoords,
  EdgeWeightType edgeWeightType,
  EdgeWeightFormat edgeWeightFormat,
  EdgeDataFormat edgeFormatData,
  NodeCoordType nodeCoordType,
  DisplayDataType dataDisplayType,
  int[][] tours
) {

  /**
   * Reads a file in TSPLIB format and returns its data.
   *
   * @param file  a file in TSPLIB format
   * @return      the instance data
   * @throws IOException  if a I/O error occurs
   */
  public static TsplibFileData read(final Path file) throws IOException {
    try (final InputStream is = new FileInputStream(file.toFile())) {
      return read(is);
    }
  }

  /**
   * Reads an input stream emitting data in TSPLIB format and returns the data.
   *
   * The caller passes the ownership of the provided input stream to this
   * method which takes care of properly closing it before returning or
   * throwing an exception.
   *
   * @param is  the source in TSPLIB format
   * @return    the instance data
   * @throws IOException  if an I/O error occurs
   */
  public static TsplibFileData read(final InputStream is) throws IOException {
    // Specification part
    String name = "NO_NAME";
    ProblemType type = null;
    String comment = "";
    int dimension = -1;
    EdgeWeightType edgeWeightType = null;
    EdgeWeightFormat edgeWeightFormat = null;
    EdgeDataFormat edgeFormatData = null;
    NodeCoordType nodeCoordType = null;
    DisplayDataType displayDataType = null;

    // Data part
    int[] depots = null;
    int[][] fixedEdges = null;
    int[] edgeWeights = null;
    double[][] nodeCoords = null;
    double[][] displayCoords = null;
    int[][] tours = null;

    try (
        is;
        final BufferedInputStream bis = new BufferedInputStream(is);
        final Scanner sc = new Scanner(bis)
    ) {
      sc.useDelimiter(TsplibFileFormat.DELIMITER);

      while (sc.hasNext()) {
        final String section = sc.next();
        switch (section) {
          // Specification part
          case "NAME" -> { name = sc.skip(TsplibFileFormat.DELIMITER).nextLine(); }
          case "TYPE" -> {
            type = ProblemType.valueOf(sc.next());
            sc.nextLine();  // some instances incorrectly report the author's name after the type
          }
          case "COMMENT" -> { comment = sc.skip(TsplibFileFormat.DELIMITER).nextLine(); }
          case "DIMENSION" -> { dimension = sc.nextInt(); }
          case "EDGE_WEIGHT_TYPE" -> { edgeWeightType = EdgeWeightType.valueOf(sc.next()); }
          case "EDGE_WEIGHT_FORMAT" -> { edgeWeightFormat = EdgeWeightFormat.valueOf(sc.next()); }
          case "EDGE_FORMAT_DATA" -> { edgeFormatData = EdgeDataFormat.valueOf(sc.next()); }
          case "NODE_COORD_TYPE" -> { nodeCoordType = NodeCoordType.valueOf(sc.next()); }
          case "DISPLAY_DATA_TYPE" -> { displayDataType = DisplayDataType.valueOf(sc.next()); }

          // Data part
          case "NODE_COORD_SECTION" -> {
            int i = 0;
            if (nodeCoordType == null) {
              final String line = sc.skip(TsplibFileFormat.DELIMITER).nextLine();
              final String[] parts = TsplibFileFormat.DELIMITER.split(line);
              final int nParts = parts.length;
              if (nParts == 3) {
                nodeCoordType = NodeCoordType.TWOD_COORDS;
                nodeCoords = new double[dimension][2];

                final int nodeIdx = Integer.parseInt(parts[0]) - 1;
                if (nodeIdx != 0) {
                  throw new TsplibFileFormatException(
                    "Instance: " + name + ", first edge in 'NODE_COORD_SECTION' " +
                    "has index " + (nodeIdx + 1) + " (expected: 1)"
                  );
                }

                final double[] coord = nodeCoords[0];
                coord[0] = Double.parseDouble(parts[1]);
                coord[1] = Double.parseDouble(parts[2]);
                i = 1;

              } else if (nParts == 4) {
                nodeCoordType = NodeCoordType.THREED_COORDS;
                nodeCoords = new double[dimension][3];

                final int nodeIdx = Integer.parseInt(parts[0]) - 1;
                if (nodeIdx != 0) {
                  throw new TsplibFileFormatException(
                    "Instance " + name + ": first edge in 'NODE_COORD_SECTION' " +
                    "has index " + (nodeIdx + 1) + " (expected: 1)"
                  );
                }

                final double[] coord = nodeCoords[0];
                coord[0] = Double.parseDouble(parts[1]);
                coord[1] = Double.parseDouble(parts[2]);
                coord[2] = Double.parseDouble(parts[3]);
                i = 1;
              } else {
                throw new TsplibFileFormatException(
                    "Instance " + name + ": found 'NODE_COORD_SECTION' with no prior " +
                    "'NODE_COORD_TYPE' section and failed to autodetect 'NODE_COORD_TYPE'"
                );
              }

              // LOGGER.warning(
              //     "Instance " + name + ": found 'NODE_COORD_SECTION' with no prior " +
              //     "'NODE_COORD_TYPE' section. 'NODE_COORD_TYPE' set to '" + nodeCoordType + "'"
              // );
            }

            switch (nodeCoordType) {
              case TWOD_COORDS -> {
                if (nodeCoords == null) {
                  nodeCoords = new double[dimension][2];
                }
                for (; i < dimension; ++i) {
                  final int nodeIdx = sc.nextInt() - 1;
                  if (nodeIdx != i) {
                    throw new TsplibFileFormatException(
                      "Instance " + name + ": edge in 'NODE_COORD_SECTION' " +
                      "has index " + (nodeIdx + 1) + " (expected: " + (i+1) + ")"
                    );
                  }
                  final double[] coords = nodeCoords[nodeIdx];
                  coords[0] = sc.nextDouble();
                  coords[1] = sc.nextDouble();
                }
              }
              case THREED_COORDS -> {
                if (nodeCoords == null) {
                  nodeCoords = new double[dimension][3];
                }
                for (; i < dimension; ++i) {
                  final int nodeIdx = sc.nextInt() - 1;
                  if (nodeIdx != i) {
                    throw new TsplibFileFormatException(
                      "Instance " + name + ": edge in 'NODE_COORD_SECTION' " +
                      "has index " + (nodeIdx + 1) + " (expected: " + (i + 1) + ")"
                    );
                  }
                  final double[] coords = nodeCoords[nodeIdx];
                  coords[0] = sc.nextDouble();
                  coords[1] = sc.nextDouble();
                  coords[2] = sc.nextDouble();
                }
              }
              case NO_COORDS -> {
                throw new TsplibFileFormatException(
                    "Instance " + name + ": found 'NODE_COORD_SECTION' " +
                    "when 'NODE_COORD_TYPE' is 'NO_COORD'"
                );
              }
            }
          }

          case "DEPOT_SECTION" -> {
            int nextDepot;
            final List<Integer> tmpDepots = new ArrayList<>(16);
            while ((nextDepot = sc.nextInt()) != -1) {
              if (nextDepot < 1 || nextDepot > dimension) {
                throw new TsplibFileFormatException(
                    "Instance " + name + ": depot with index " + nextDepot + " in 'DEPOT_SECTION'"
                );
              }
              tmpDepots.add(nextDepot - 1);
            }

            int i = -1;
            depots = new int[tmpDepots.size()];
            for (final int d: tmpDepots)  depots[++i] = d;
          }

          case "FIXED_EDGES_SECTION" -> {
            int firstNode;
            final List<int[]> tmpFixedEdges = new ArrayList<>(16);
            while ((firstNode = sc.nextInt()) != -1) {
              if (firstNode < 1 || firstNode > dimension) {
                throw new TsplibFileFormatException(
                  "Instance " + name + ": node with index " + firstNode + " in 'FIXED_EDGES_SECTION'"
                );
              }

              final int otherNode = sc.nextInt();
              if (otherNode < 1 || otherNode > dimension) {
                throw new TsplibFileFormatException(
                  "Instance " + name + ": node with index " + otherNode + " in 'FIXED_EDGES_SECTION'"
                );
              }

              tmpFixedEdges.add(new int[] { firstNode - 1, otherNode - 1 });
            }

            int i = -1;
            fixedEdges = new int[tmpFixedEdges.size()][];
            for (final int[] e: tmpFixedEdges)  fixedEdges[++i] = e;
          }

          case "DISPLAY_DATA_SECTION" -> {
            switch (displayDataType) {
              case TWOD_DISPLAY -> {
                displayCoords = new double[dimension][2];
                for (int i = 0; i < dimension; ++i) {
                  final int nodeIdx = sc.nextInt() - 1;
                  if (nodeIdx != i) {
                    throw new TsplibFileFormatException(
                      "Instance " + name + ": edge in 'NODE_COORD_SECTION' " +
                      "has index " + (nodeIdx + 1) + " (expected: " + (i + 1) + ")"
                    );
                  }
                  final double[] dCoords = displayCoords[i];
                  dCoords[0] = sc.nextDouble();
                  dCoords[1] = sc.nextDouble();
                }
              }

              case COORD_DISPLAY, NO_DISPLAY -> {
                throw new TsplibFileFormatException(
                    "Found 'DISPLAY_DATA_SECTION' when 'DATA_DISPLAY_TYPE' is " + displayDataType
                );
              }
            }
          }

          case "EDGE_WEIGHT_SECTION" -> {
            final int nEdges = dimension * (dimension - 1) / 2;
            edgeWeights = new int[nEdges];

            switch (edgeWeightFormat) {
              case FULL_MATRIX -> {
                int k = -1;
                for (int i = 0; i < dimension; ++i) {
                  for (int j = 0; j < dimension; ++j) {
                    final int weight = sc.nextInt();
                    if (i < j) {
                      edgeWeights[++k] = weight;
                    } else if (i == j) {
                      if (weight != 0) {
                        throw new TsplibFileFormatException(
                            "Instance " + name + ": non-zero edge weight (" + weight +
                            ") on the diagonal at (" + i + "," + j + ")"
                        );
                      }
                    } else {
                      final int l = TsplibUtil.strictUpperTriangularToArrayIndex(j, i, dimension);
                      if (weight != edgeWeights[l]) {
                        throw new TsplibFileFormatException(
                            "Instance " + name + ": edge weights are not symmetric, " +
                            "w[" + i + "," + j + "] = " + weight + " != " +
                            edgeWeights[l] + " = w[" + j + "," + i + "]"
                        );
                      }
                    }
                  }
                }
              }

              case FUNCTION -> {
                throw new TsplibFileFormatException(
                    "Found 'EDGE_WEIGHT_SECTION' when 'EDGE_WEIGHT_FORMAT' is 'FUNCTION'"
                );
              }

              case LOWER_COL -> {
                final int n = dimension - 1;
                for (int j = 0; j < n; ++j) {
                  for (int i = j + 1; i < n; ++i) {
                    final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, dimension);
                    edgeWeights[k] = sc.nextInt();
                  }
                }
              }

              case LOWER_DIAG_COL -> {
                final int n = dimension - 1;
                for (int j = 0; j < n; ++j) {
                  final int diagWeight = sc.nextInt();
                  if (diagWeight != 0) {
                    throw new TsplibFileFormatException(
                        "Instance " + name + ": non-zero diagonal weight at (" + j + "," + j + ")"
                    );
                  }

                  for (int i = j + 1; i < n; ++i) {
                    final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, dimension);
                    edgeWeights[k] = sc.nextInt();
                  }
                }
              }

              case LOWER_DIAG_ROW -> {
                for (int i = 0; i < dimension; ++i) {
                  for (int j = 0; j < i; ++j) {
                    final int k = TsplibUtil.strictUpperTriangularToArrayIndex(j, i, dimension);
                    edgeWeights[k] = sc.nextInt();;
                  }

                  final int diagWeight = sc.nextInt();
                  if (diagWeight != 0) {
                    throw new TsplibFileFormatException(
                        "Instance " + name + ": non-zero diagonal weight at (" + i + "," + i + ")"
                    );
                  }
                }
              }

              case LOWER_ROW -> {
                for (int i = 1; i < dimension; ++i) {
                  for (int j = 0; j < i; ++j) {
                    final int k = TsplibUtil.strictUpperTriangularToArrayIndex(j, i, dimension);
                    edgeWeights[k] = sc.nextInt();
                  }
                }
              }

              case UPPER_COL -> {
                for (int j = 1; j < dimension; ++j) {
                  for (int i = 0; i < j; ++i) {
                    final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, dimension);
                    edgeWeights[k] = sc.nextInt();
                  }
                }
              }

              case UPPER_DIAG_COL -> {
                for (int j = 0; j < dimension; ++j) {
                  for (int i = 0; i < j; ++i) {
                    final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, dimension);
                    edgeWeights[k] = sc.nextInt();
                  }

                  final int diagWeight = sc.nextInt();
                  if (diagWeight != 0) {
                    throw new TsplibFileFormatException(
                        "Instance " + name + ": non-zero diagonal weight at (" + j + "," + j + ")"
                    );
                  }
                }
              }

              case UPPER_DIAG_ROW -> {
                for (int i = 0; i < dimension; ++i) {
                  final int diagWeight = sc.nextInt();
                  if (diagWeight != 0) {
                    throw new TsplibFileFormatException(
                        "Instance " + name + ": non-zero diagonal weight at (" + i + "," + i + ")"
                    );
                  }

                  for (int j = i + 1; j < dimension; ++j) {
                    final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, dimension);
                    edgeWeights[k] = sc.nextInt();;
                  }
                }
              }

              case UPPER_ROW -> {
                final int n = dimension - 1;
                for (int i = 0; i < n; ++i) {
                  for (int j = i + 1; j < dimension; ++j) {
                    final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, dimension);
                    edgeWeights[k] = sc.nextInt();
                  }
                }
              }

              default -> {
                throw new TsplibFileFormatException(
                    "Unhandled 'EDGE_WEIGHT_SECTION' type: " + edgeWeightType
                );
              }
            }
          }

          case "EDGE_DATA_SECTION" -> {
            throw new TsplibFileFormatException("'EDGE_DATA_SECTION' is not (yet) supported");
          }

          case "TOUR_SECTION" -> {
            int nodeIdx; 
            final List<int[]> tmpTours = new ArrayList<>();

            if (dimension < 0) {
              // The data file does not declare the dimension of the tour (e.g. rd100.opt.tour),
              final List<Integer> tmpTour = new ArrayList<>(1024);
              while (sc.hasNext() && (nodeIdx = sc.nextInt()) != -1)  tmpTour.add(nodeIdx);

              dimension = tmpTour.size();
              final int[] tour = new int[dimension];
              for (int i = 0; i < dimension; ++i)  tour[i] = tmpTour.get(i);
              tmpTours.add(tour);
            }

            while (sc.hasNext() && sc.hasNextInt() && (nodeIdx = sc.nextInt()) != -1) {
              final int[] tour = new int[dimension];
              tour[0] = nodeIdx;
              
              int i = 0;
              while (sc.hasNext() && (nodeIdx = sc.nextInt()) != -1)  tour[++i] = nodeIdx;

              if (++i != dimension) {
                throw new TsplibFileFormatException(
                    "Tour " + (tmpTours.size() + 1) + " has " + i + " nodes, expected " + dimension
                ); 
              }

              tmpTours.add(tour);
            }

            tours = tmpTours.toArray(new int[tmpTours.size()][]);
          }

          case "EOF" -> { /* no-op */ }

          default -> {
            throw new TsplibFileFormatException("Unexpected file section: " + section);
          }
        }
      }
    }

    return new TsplibFileData(
        name, type, comment, dimension, depots, fixedEdges, edgeWeights, nodeCoords, displayCoords,
        edgeWeightType, edgeWeightFormat, edgeFormatData, nodeCoordType, displayDataType, tours
    );
  }
}