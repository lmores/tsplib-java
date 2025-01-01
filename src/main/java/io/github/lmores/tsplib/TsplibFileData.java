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
 * Store the data found when reading a file in the TSPLIB format.
 *
 * @param name              the name of the instance
 * @param type              the type of problem
 * @param comment           the comment associated with the instance
 * @param dimension         the number of nodes (and depotes) for ATSP, TSP and CVRP
 * @param capacity          the truck capacity (CVRP only)
 * @param edgeWeightType    how edge weights are computed
 * @param edgeWeightFormat  how edge weight are provided
 * @param edgeDataFormat    how edges are provided
 * @param nodeCoordType     how node coordinates are provided
 * @param displayDataType   how nodes should be displayed
 * @param nodeCoords        the coordinates of the nodes
 * @param depots            the number of depots in a CVRP
 * @param demands           the demand fo each node (including depots) in a CVRP
 * @param edges             the edges of the graph encoded according to {@code edgeDataFormat}
 * @param fixedEdges        the list of fixed edges
 * @param edgeWeights       the edge weights
 * @param displayCoords     node coordinates for graph representation only
 * @param tours             a list of tours
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public record TsplibFileData(
    // Specification part
    String name,
    ProblemType type,
    String comment,
    int dimension,
    int capacity,
    EdgeWeightType edgeWeightType,
    EdgeWeightFormat edgeWeightFormat,
    EdgeDataFormat edgeDataFormat,
    NodeCoordType nodeCoordType,
    DisplayDataType displayDataType,

    // Data part
    double[][] nodeCoords,
    int[] depots,
    int[] demands,
    int[][] edges,
    int[][] fixedEdges,
    int[][] edgeWeights,
    double[][] displayCoords,
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
    if (is == null) {
      throw new IllegalArgumentException("Input stream is null");
    }

    // Specification part
    String name = null;
    ProblemType type = null;
    String comment = "";
    int dimension = -1;
    int capacity = -1;
    EdgeWeightType edgeWeightType = null;
    EdgeWeightFormat edgeWeightFormat = null;
    EdgeDataFormat edgeDataFormat = null;
    NodeCoordType nodeCoordType = null;
    DisplayDataType displayDataType = DisplayDataType.NO_DISPLAY;

    // Data part
    double[][] nodeCoords = null;
    int[] depots = null;
    int[] demands = null;
    int[][] edges = null;
    int[][] fixedEdges = null;
    int[][] edgeWeights = null;
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
          case "CAPACITY" -> { capacity = sc.nextInt(); }
          case "EDGE_WEIGHT_TYPE" -> { edgeWeightType = EdgeWeightType.valueOf(sc.next()); }
          case "EDGE_WEIGHT_FORMAT" -> { edgeWeightFormat = EdgeWeightFormat.valueOf(sc.next()); }
          case "EDGE_DATA_FORMAT" -> { edgeDataFormat = EdgeDataFormat.valueOf(sc.next()); }
          case "NODE_COORD_TYPE" -> { nodeCoordType = NodeCoordType.valueOf(sc.next()); }
          case "DISPLAY_DATA_TYPE" -> { displayDataType = DisplayDataType.valueOf(sc.next()); }

          // Data part
          case "NODE_COORD_SECTION" -> {
            nodeCoords = new double[dimension][];

            int i = 0;
            if (nodeCoordType == null) {
              // Sniff coordinate type
              final String line = sc.skip(TsplibFileFormat.DELIMITER).nextLine();
              final String[] parts = TsplibFileFormat.DELIMITER.split(line);
              final int nParts = parts.length;

              if (nParts == 3) {
                nodeCoordType = NodeCoordType.TWOD_COORDS;

                final int nodeIdx = Integer.parseInt(parts[0]) - 1;
                if (nodeIdx != 0) {
                  throw new TsplibFileFormatException(
                    "Instance: " + name + ", first edge in 'NODE_COORD_SECTION' " +
                    "has index " + (nodeIdx + 1) + " (expected: 1)"
                  );
                }

                nodeCoords[0] = new double[] {
                    Double.parseDouble(parts[1]),
                    Double.parseDouble(parts[2])
                };
                i = 1;

              } else if (nParts == 4) {
                nodeCoordType = NodeCoordType.THREED_COORDS;

                final int nodeIdx = Integer.parseInt(parts[0]) - 1;
                if (nodeIdx != 0) {
                  throw new TsplibFileFormatException(
                    "Instance " + name + ": first edge in 'NODE_COORD_SECTION' " +
                    "has index " + (nodeIdx + 1) + " (expected: 1)"
                  );
                }

                nodeCoords[0] = new double[] {
                    Double.parseDouble(parts[1]),
                    Double.parseDouble(parts[2]),
                    Double.parseDouble(parts[3])
                };
                i = 1;

              } else {
                throw new TsplibFileFormatException(
                    "Instance " + name + ": found 'NODE_COORD_SECTION' with no prior " +
                    "'NODE_COORD_TYPE' section and failed to autodetect 'NODE_COORD_TYPE'"
                );
              }
            }

            switch (nodeCoordType) {
              case TWOD_COORDS -> {
                for (; i < dimension; ++i) {
                  final int nodeIdx = sc.nextInt() - 1;
                  if (nodeIdx != i) {
                    throw new TsplibFileFormatException(
                        "Instance " + name + ": found node " + (nodeIdx + 1) +
                        " in 'NODE_COORD_SECTION', expected: " + (i + 1)
                    );
                  }
                  nodeCoords[nodeIdx] = new double[] {sc.nextDouble(), sc.nextDouble()};
                }
              }

              case THREED_COORDS -> {
                for (; i < dimension; ++i) {
                  final int nodeIdx = sc.nextInt() - 1;
                  if (nodeIdx != i) {
                    throw new TsplibFileFormatException(
                        "Instance " + name + ": found node " + (nodeIdx + 1) +
                        " in 'NODE_COORD_SECTION', expected: " + (i + 1)
                    );
                  }
                  nodeCoords[nodeIdx] = new double[] {sc.nextDouble(), sc.nextDouble(), sc.nextDouble()};
                }
              }

              case NO_COORDS -> {
                throw new TsplibFileFormatException(
                    "Instance " + name + ": found 'NODE_COORD_SECTION' but NODE_COORD_TYPE == NO_COORD"
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
                    "Instance " + name + ": found depot " + nextDepot + " in 'DEPOT_SECTION'"
                );
              }
              tmpDepots.add(nextDepot - 1);
            }

            int i = -1;
            depots = new int[tmpDepots.size()];
            for (final int d: tmpDepots)  depots[++i] = d;
          }

          case "DEMAND_SECTION" -> {
            demands = new int[dimension];
            for (int i = 0; i < dimension; ++i) {
              final int nodeIdx = sc.nextInt() - 1;
              demands[nodeIdx] = sc.nextInt();
            }
          }

          case "EDGE_DATA_SECTION" -> {
            switch (edgeDataFormat) {
              case ADJ_LIST -> {
                final List<int[]> tmpEdges = new ArrayList<>();  // TODO? initial capacity

                int firstNode;
                while ((firstNode = sc.nextInt()) != -1) {
                  final List<Integer> tmpAdjacentNodes = new ArrayList<>(32);
                  tmpAdjacentNodes.add(firstNode - 1);

                  int node;
                  while ((node = sc.nextInt()) != -1) {
                    tmpAdjacentNodes.add(node - 1);
                  }

                  int i = -1;
                  final int[] adjacentNodes = new int[tmpAdjacentNodes.size()];
                  for (final int x: tmpAdjacentNodes)  adjacentNodes[++i] = x;

                  tmpEdges.add(adjacentNodes);
                }

                edges = tmpEdges.toArray(new int[0][]);
              }

              case EDGE_LIST -> {
                final List<int[]> tmpEdges = new ArrayList<>();  // TODO? initial capacity

                int firstNode;
                while ((firstNode = sc.nextInt()) != -1) {
                  final int[] edge = new int[] {firstNode - 1, sc.nextInt() - 1};
                  tmpEdges.add(edge);
                }

                edges = tmpEdges.toArray(new int[0][]);
              }

              case null -> {
                throw new TsplibFileFormatException(
                    "Instance " + name + ": found 'EDGE_DATA' section but 'EDGE_DATA_FORMAT' is null"
                );
              }
            }
          }

          // alb4000.hcp contains "FIXED_EDGES" rather than "FIXED_EDGES_SECTION"
          case "FIXED_EDGES", "FIXED_EDGES_SECTION" -> {
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

              tmpFixedEdges.add(new int[] {firstNode - 1, otherNode - 1});
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
                      "Instance " + name + ": found node " + (nodeIdx + 1) +
                      " in 'NODE_COORD_SECTION', expected: " + (i + 1)
                    );
                  }
                  final double[] dCoords = displayCoords[i];
                  dCoords[0] = sc.nextDouble();
                  dCoords[1] = sc.nextDouble();
                }
              }

              case COORD_DISPLAY, NO_DISPLAY -> {
                throw new TsplibFileFormatException(
                    "Found 'DISPLAY_DATA_SECTION' but DATA_DISPLAY_TYPE == " + displayDataType
                );
              }
            }
          }

          case "EDGE_WEIGHT_SECTION" -> {
            edgeWeights = new int[dimension][dimension];

            switch (edgeWeightFormat) {
              case FULL_MATRIX -> {
                for (int i = 0; i < dimension; ++i) {
                  for (int j = 0; j < dimension; ++j) {
                    edgeWeights[i][j] = sc.nextInt();
                  }
                }
              }

              case FUNCTION -> {
                throw new TsplibFileFormatException(
                    "Found 'EDGE_WEIGHT_SECTION' but EDGE_WEIGHT_FORMAT == FUNCTION"
                );
              }

              case LOWER_COL -> {
                for (int j = 0, n = dimension - 1; j < n; ++j) {
                  for (int i = j + 1; i < dimension; ++i) {
                    edgeWeights[i][j] = edgeWeights[j][i] = sc.nextInt();
                  }
                }
              }

              case LOWER_DIAG_COL -> {
                for (int j = 0; j < dimension; ++j) {
                  for (int i = j; i < dimension; ++i) {
                    edgeWeights[i][j] = edgeWeights[j][i] = sc.nextInt();
                  }
                }
              }

              case LOWER_DIAG_ROW -> {
                for (int i = 0; i < dimension; ++i) {
                  for (int j = 0; j <= i; ++j) {
                    edgeWeights[i][j] = edgeWeights[j][i] = sc.nextInt();;
                  }
                }
              }

              case LOWER_ROW -> {
                for (int i = 1; i < dimension; ++i) {
                  for (int j = 0; j < i; ++j) {
                    edgeWeights[i][j] = edgeWeights[j][i] = sc.nextInt();
                  }
                }
              }

              case UPPER_COL -> {
                for (int j = 1; j < dimension; ++j) {
                  for (int i = 0; i < j; ++i) {
                    edgeWeights[i][j] = edgeWeights[j][i] = sc.nextInt();
                  }
                }
              }

              case UPPER_DIAG_COL -> {
                for (int j = 0; j < dimension; ++j) {
                  for (int i = 0; i <= j; ++i) {
                    edgeWeights[i][j] = edgeWeights[j][i] = sc.nextInt();
                  }
                }
              }

              case UPPER_DIAG_ROW -> {
                for (int i = 0; i < dimension; ++i) {
                  for (int j = i; j < dimension; ++j) {
                    edgeWeights[i][j] = edgeWeights[j][i] = sc.nextInt();;
                  }
                }
              }

              case UPPER_ROW -> {
                for (int i = 0, n = dimension - 1; i < n; ++i) {
                  for (int j = i + 1; j < dimension; ++j) {
                    edgeWeights[i][j] = edgeWeights[j][i] = sc.nextInt();
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

          case "TOUR_SECTION" -> {
            int node;
            final List<int[]> tmpTours = new ArrayList<>();

            if (dimension < 0) {
              // The data file does not declare the dimension of the tour (e.g. rd100.opt.tour)
              final List<Integer> tmpTour = new ArrayList<>(1024);
              while (sc.hasNext() && (node = sc.nextInt()) != -1)  tmpTour.add(node - 1);

              dimension = tmpTour.size();
              final int[] tour = new int[dimension];
              for (int i = 0; i < dimension; ++i)  tour[i] = tmpTour.get(i);
              tmpTours.add(tour);
            }

            // As this section often ends with 'EOF', sc.hasNextInt() is needed
            while (sc.hasNextInt() && (node = sc.nextInt()) != -1) {
              final int[] tour = new int[dimension];
              tour[0] = node - 1;

              int i = 0;
              while (sc.hasNext() && (node = sc.nextInt()) != -1)  tour[++i] = node - 1;

              if (++i != dimension) {
                throw new TsplibFileFormatException(
                    "Tour " + (tmpTours.size() + 1) + " has " + i + " nodes, expected " + dimension
                );
              }

              tmpTours.add(tour);
            }

            tours = tmpTours.toArray(new int[0][0]);
          }

          case "EOF" -> { /* no-op */ }

          default -> {
            throw new TsplibFileFormatException("Unexpected section: " + section);
          }
        }
      }
    }

    return new TsplibFileData(
        name, type, comment, dimension, capacity,
        edgeWeightType, edgeWeightFormat, edgeDataFormat, nodeCoordType, displayDataType,
        nodeCoords, depots, demands, edges, fixedEdges, edgeWeights, displayCoords, tours
    );
  }
}