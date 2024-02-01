package io.github.lmores.tsplib.tsp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiFunction;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.DisplayDataType;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeDataFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;
import io.github.lmores.tsplib.TsplibFileFormat.NodeCoordType;

/**
 * Common methods for all implementations of TSP instances.
 * Subclasses differs only in the strategy adopted to compute edge weights.
 * <p>
 * The following assumptions hold for any concrete implementation of this class:
 * <ol>
 *    <li>Nodes are represented using 0-based indexes
 *        (from {@code 0} to {@code dimension - 1} inclusive)
 *    </li>
 *    <li>Graphs are always complete, hence there always exists an edge joining
 *        node {@code i} and node {@code j} for each
 *        {@code 0 <= i,j <= dimension - 1} and {@code i != j}.
 *    </li>
 * </ol>
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public abstract class TspInstance {
  private final String name;
  private final String comment;
  private final int dimension;
  private final int[] depots;
  private final int[][] fixedEdges;
  private final double[][] nodeCoords;
  private final double[][] displayCoords;
  private final EdgeWeightType edgeWeightType;
  private final EdgeWeightFormat edgeWeightFormat;
  private final EdgeDataFormat edgeDataFormat;
  private final NodeCoordType nodeCoordType;
  private final DisplayDataType displayDataType;

  /**
   * Initialize a TSP instance using the values provided as arguments.
   *
   * @param name              the name of the instance
   * @param comment           a comment
   * @param dimension            number of nodes
   * @param edgeWeightType    how edge weights are computed
   * @param edgeWeightFormat  how edge weight are provided
   * @param edgeFormatData    how edges are provided
   * @param nodeCoordType     how node coordinates are provided
   * @param dataDisplayType   how nodes should be displayed
   * @param nodeCoords        node coordinates
   * @param depots            declared depots
   * @param fixedEdges        list of fixed edges
   * @param displayCoords     node coordinates for graph representation only
   */
  protected TspInstance(
      final String name, final String comment, final int dimension,
      final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
      final EdgeDataFormat edgeFormatData, final NodeCoordType nodeCoordType,
      final DisplayDataType dataDisplayType,
      final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
      final double[][] displayCoords
  ) {
    this.name = name;
    this.comment = comment;
    this.dimension = dimension;
    this.edgeWeightType = edgeWeightType;
    this.edgeWeightFormat = edgeWeightFormat;
    this.edgeDataFormat = edgeFormatData;
    this.nodeCoordType = nodeCoordType;
    this.displayDataType = dataDisplayType;
    this.nodeCoords = nodeCoords;
    this.depots = depots;
    this.fixedEdges = fixedEdges;
    this.displayCoords = displayCoords;
  }

  /**
   * Initialize a TSP instance using data from the content of a TSPLIB file.
   *
   * @param data  the instance data
   */
  protected TspInstance(final TsplibFileData data) {
    this(
        data.name(), data.comment(), data.dimension(),
        data.edgeWeightType(), data.edgeWeightFormat(), data.edgeFormatData(),
        data.nodeCoordType(), data.dataDisplayType(), data.nodeCoords(),
        data.depots(), data.fixedEdges(), data.displayCoords()
    );
  }

  /**
   * Returns a TSP instance backed by the provided data.
   *
   * @param data  the instance data
   * @return      a TSP instance
   */
  public static TspInstance from(final TsplibFileData data) {
    return from(data, null);
  }

  /**
   * Returns a TSP instance backed by the provided data using a user-defined
   * function to compute edge weights.
   * <p>
   * The edge weight function accepts two arrays of double values and returns
   * the weight value as a double.
   * To compute the weight of the edge joining nodes {@code i} and {@code j}
   * the provided function is called using as argument the coordinates of nodes
   * {@code i} and {@code j}.
   *
   * @param data            the instance data
   * @param edgeWeightFunc  a user-defined function to compute edge weights
   * @return                a TSP instance
   */
  public static TspInstance from(
      final TsplibFileData data, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) {
    final EdgeWeightType edgeWeightType = data.edgeWeightType();
    if (edgeWeightType == EdgeWeightType.SPECIAL && edgeWeightFunc == null) {
      throw new IllegalArgumentException(
          "Instance '" + data.name() + "': edge weight format is 'SPECIAL', " +
          "but the user-defined function is null"
      );
    }

    return switch(edgeWeightType) {
      case EXPLICIT -> new ExplicitTspInstance(data);
      case ATT -> new PseudoEuclidean2dTspInstance(data);
      case CEIL_2D -> new CeilEuclidean2dTspInstance(data);
      case EUC_2D -> new Euclidean2dTspInstance(data);
      case EUC_3D -> new Euclidean3dTspInstance(data);
      case GEO -> new Geographic2dTspInstance(data);
      case MAN_2D -> new Manhattan2dTspInstance(data);
      case MAN_3D -> new Manhattan3dTspInstance(data);
      case MAX_2D -> new Max2dTspInstance(data);
      case MAX_3D -> new Max3dTspInstance(data);
      case SPECIAL -> new Special2dTspInstance(data, edgeWeightFunc);
      case XRAY1, XRAY2 -> throw new UnsupportedOperationException(
          "Unimplemented edge weight type: " + edgeWeightType
      );
      default -> throw new IllegalArgumentException(
          "Unexpected edge weight type: " + edgeWeightType
      );
    };
  }

  /**
   * Loads a TSP instance from a file in TSPLIB format.
   *
   * @param file  the file containing the instance data
   * @return      the TSP instance
   * @throws IOException  if a I/O error occurs
   */
  public static TspInstance read(final Path file) throws IOException {
    return read(file, null);
  }

  /**
   * Loads a TSP instance from a file in TSPLIB format using the provided
   * function to compute edge weights if the edge weight format is 'SPECIAL'.
   *
   * @param file            the file containing the instance data
   * @param edgeWeightFunc  a custom function to compute edge weights
   * @return                the TSP instance
   * @throws IOException    if a I/O error occurs
   */
  public static TspInstance read(
      final Path file, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) throws IOException {
    return TspInstance.from(TsplibFileData.read(file), edgeWeightFunc);
  }

  /**
   * Returns the name of the instance.
   *
   * @return the name of the instance
   */
  public String getName() { return name; }

  /**
   * Returns the comment associated with the instance.
   *
   * @return the comment associated with the instance
   */
  public String getComment() { return comment; }

  /**
   * Returns the number of nodes.
   *
   * @return the number of nodes
   */
  public int getDimension() { return dimension; }

  /**
   * Returns the nodes to be used as depots.
   *
   * @return the nodes to be used as depots
   */
  public int[] getDepots() { return depots; }

  /**
   * Returns the edges that must be traversed.
   *
   * @return the edges that must be traversed
   */
  public int[][] getFixedEdges() { return fixedEdges; }

  /**
   * Returns the node coordinates (used to compute edge weights).
   *
   * @return the node coordinates (used to compute edge weights)
   */
  public double[][] getNodeCoords() { return nodeCoords; }

  /**
   * Returns the node coordinates (only used for graphical display).
   *
   * @return the node coordinates (only used for graphical display).
   */
  public double[][] getDisplayCoords() { return displayCoords; }

  /**
   * Returns the rule used to compute edge weights.
   *
   * @return the rule used to compute edge weights.
   */
  public EdgeWeightType getEdgeWeightType() { return edgeWeightType; }

  /**
   * Returns the format used to encode edge weights.
   *
   * @return the format used to encode edge weights
   */
  public EdgeWeightFormat getEdgeWeightFormat() { return edgeWeightFormat; }

  /**
   * Returns the format used to encode the edge of the graph.
   *
   * @return the format used to encode the edge of the graph
   */
  public EdgeDataFormat getEdgeDataFormat() { return edgeDataFormat; }

  /**
   * Returns the type of coordinates associated with the nodes of the graph.
   *
   * @return the type of coordinates associated with the nodes of the graph.
   */
  public NodeCoordType getNodeCoordType() { return nodeCoordType; }

  /**
   * Returns how a graphical display of the nodes can be obtained.
   *
   * @return how a graphical display of the nodes can be obtained
   */
  public DisplayDataType getDisplayDataType() { return displayDataType; }

  /**
   * Returns the value of the weight associated with the unordered edge joining
   * nodes {@code i} and {@code j}.
   *
   * @param i  0-based index of one node of the edge
   * @param j  0-based index of the other node of the edge
   * @return   the weight of the edge
   */
  public abstract int getEdgeWeight(final int i, final int j);

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   * Since each {@link TspInstance} is defined on a complete graph, this method
   * simply checks that {@code 0 <= i,j < dimension} and {@code i != j}.
   *
   * @param i  the 0-based index of a node
   * @param j  the 0-based index of the other node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  public boolean hasEdge(final int i, final int j) {
    return 0 <= i && i < dimension && 0 <= j && j < dimension && i != j;
  }

  /**
   * Returns a copy of the edge weights as an array representing the strict
   * upper diagonal of a matrix of size {@code dimension}.
   * The returned array is not retained.
   *
   * @return  an array containing edge weights
   */
  public int[] materializeEdgeWeights() {
    int k = -1;
    final int[] edgeWeights = new int[dimension];
    for (int i = 0; i < dimension; ++i) {
      for (int j = i + 1; j < dimension; ++j) {
        edgeWeights[++k] = getEdgeWeight(i, j);
      }
    }

    return edgeWeights;
  }

  /**
   * Returns a copy of the edge weights as a symmetric matrix of size
   * {@code dimension}. The returned matrix is not retained.
   *
   * @return  a symmetric matrix containing edge weights
   */
  public int[][] materializeEdgeWeightsMatrix() {
    final int[][] edgeWeights = new int[dimension][dimension];
    for (int i = 0; i < dimension; ++i) {
      for (int j = i + 1; j < dimension; ++j) {
        edgeWeights[i][j] = edgeWeights[i][j] = getEdgeWeight(i, j);
      }
    }

    return edgeWeights;
  }
}
