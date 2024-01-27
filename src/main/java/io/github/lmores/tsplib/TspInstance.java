package io.github.lmores.tsplib;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.function.BiFunction;

import io.github.lmores.tsplib.TsplibFileFormat.DisplayDataType;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeFormatData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;
import io.github.lmores.tsplib.TsplibFileFormat.NodeCoordType;
import io.github.lmores.tsplib.TsplibFileFormat.ProblemType;

/**
 * Define the common interface for all implementations of TSP instances.
 * Subclasses of this abstract class differs only in the strategy adopted to
 * compute edge weights.
 *
 * The following assumptions hold for any concrete implementation of this class:
 * <ol>
 *    <li>Nodes are representaetd using 0-based indexes
 *        (from {@code 0} to {@code nNodes - 1} inclusive)
 *    </li>
 *    <li>Graphs are always complete, hence there always exists an edge joining
 *        node {@code i} and node {@code j} for each
 *        {@code 0 <= i,j <= nNodes - 1} and {@code i != j}.
 *    </li>
 * </ol>
 *
 * @author   Lorenzo Moreschini
 * @version  %I%
 * @since    0.0.1
 */
public abstract class TspInstance {
  // Specification part
  final String name;
  final ProblemType type;
  final String comment;
  final int nNodes;
  final EdgeWeightType edgeWeightType;
  final EdgeWeightFormat edgeWeightFormat;
  final EdgeFormatData edgeFormatData;
  final NodeCoordType nodeCoordType;
  final DisplayDataType dataDisplayType;

  // Data part
  final double[][] nodeCoords;
  final int[] depots;
  final int[][] fixedEdges;
  final double[][] displayCoords;

  /** Construct a TSP instance using the values provided as arguments. */
  protected TspInstance(
      final String name, final ProblemType type, final String comment, final int nNodes,
      final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
      final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
      final DisplayDataType dataDisplayType,
      final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
      final double[][] displayCoords
  ) {
    // Specification
    this.name = name;
    this.type = type;
    this.comment = comment;
    this.nNodes = nNodes;
    this.edgeWeightType = edgeWeightType;
    this.edgeWeightFormat = edgeWeightFormat;
    this.edgeFormatData = edgeFormatData;
    this.nodeCoordType = nodeCoordType;
    this.dataDisplayType = dataDisplayType;

    // Data
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
        data.name(), data.type(), data.comment(), data.dimension(),
        data.edgeWeightType(), data.edgeWeightFormat(), data.edgeFormatData(),
        data.nodeCoordType(), data.dataDisplayType(), data.nodeCoords(),
        data.depots(), data.fixedEdges(), data.displayCoords()
    );
  }

  /**
   * Returns a TSP instance backed by the provided data.
   *
   * @param data  the instance data
   */
  static TspInstance from(final TsplibFileData data) {
    return from(data, null);
  }

  /**
   * Returns a TSP instance backed by the provided data using a user-defined
   * funtion to compute edge weights.
   *
   * The edge weight function accepts two arrays of double values and returns
   * the weight as a double.
   * To compute the weight of the edge joining nodes {@code i} and {@code j}
   * the provided function is called using as argument the coordinates of nodes
   * {@code i} and {@code j}.
   *
   * @param data            the instance data
   * @param edgeWeightFunc  a user-defined function to compute edge weights
   * @return                a TSP instance
   */
  static TspInstance from(
      final TsplibFileData data, final BiFunction<double[], double[], Double> edgeWeightFunc
  ) {
    final EdgeWeightType edgeWeightType = data.edgeWeightType();
    if (edgeWeightType == EdgeWeightType.SPECIAL && edgeWeightFunc == null) {
      throw new IllegalArgumentException(
          "Instance '" + data.name() + "' declares 'SPECIAL' as edge weight format, " +
          "but no user-defined function has been provided"
      );
    }

    return switch(edgeWeightType) {
      case EXPLICIT -> new ExplicitTspLibInstance(data);
      case ATT -> new PseudoEuclidean2dTspLibInstance(data);
      case CEIL_2D -> new CeilEuclidean2dTspLibInstance(data);
      case EUC_2D -> new Euclidean2dTspLibInstance(data);
      case EUC_3D -> new Euclidean3dTspLibInstance(data);
      case GEO -> new Geographic2dTspLibInstance(data);
      case MAN_2D -> new Manhattan2dTspLibInstance(data);
      case MAN_3D -> new Manhattan3dTspLibInstance(data);
      case MAX_2D -> new Max2dTspLibInstance(data);
      case MAX_3D -> new Max3dTspLibInstance(data);
      case SPECIAL -> new Special2dTspLibInstance(data, edgeWeightFunc);
      case XRAY1 -> throw new UnsupportedOperationException("Unimplemented case: " + data.edgeWeightType());
      case XRAY2 -> throw new UnsupportedOperationException("Unimplemented case: " + data.edgeWeightType());
      default -> throw new IllegalArgumentException("Unexpected value: " + data.edgeWeightType());
    };
  }

  /**
   * Loads a TSP instance from a file in TSPLIB format.
   *
   * @param file  the file containing the instance data
   * @return      the TSP instance
   * @throws IOException
   */
  public static TspInstance read(final Path file) throws IOException {
    return read(file, null);
  }

  /**
   * Loads a TSP instance from a file in TSPLIB format using the provided
   * function to compute edge weights if the edge weight format is 'SPECIAL'.
   *
   * @param file  the file containing the instance data
   * @return      the TSP instance
   * @throws IOException
   */
  public static TspInstance read(
      final Path file, final BiFunction<double[], double[], Double> edgeWeightFunc
  ) throws IOException {
    return TspInstance.from(TsplibFileData.read(file), edgeWeightFunc);
  }

  public String getName() { return name; }
  public ProblemType getType() { return type; }
  public String getComment() { return comment; }
  public int getnNodes() { return nNodes; }
  public EdgeWeightType getEdgeWeightType() { return edgeWeightType; }
  public EdgeWeightFormat getEdgeWeightFormat() { return edgeWeightFormat; }
  public EdgeFormatData getEdgeFormatData() { return edgeFormatData; }
  public NodeCoordType getNodeCoordType() { return nodeCoordType; }
  public DisplayDataType getDataDisplayType() { return dataDisplayType; }
  public double[][] getNodeCoords() { return nodeCoords; }
  public int[] getDepots() { return depots; }
  public int[][] getFixedEdges() { return fixedEdges; }
  public double[][] getDisplayCoords() { return displayCoords; }

  /**
   * Returns the value of the weight associated with the unordered edge joining
   * nodes {@code i} and {@code j}.
   *
   * @param i  0-based index of one node of the edge
   * @param j  0-based index of the other node of the edge
   * @return   the weight of the edge
   */
  public abstract double getEdgeWeight(final int i, final int j);

  /**
   * Check whether or not the present instance contains and edge joining nodes
   * {@code i} and {@code j}.
   * Since each {@link TspInstance} is built on a complete graph, this method
   * simply checks that {@code 0 <= i,j < nNodes} and {@code i != j}.
   *
   * @param i  the 0-based index of first the node
   * @param j  the 0-based index of second the node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  public boolean hasEdge(final int i, final int j) {
    return 0 <= i && i < nNodes && 0 <= j && j < nNodes && i != j;
  }

  /**
   * Returns a copy of edge weights encoded as an array representing the
   * strict upper diagonal of a matrix of size {@code nNodes}.
   *
   * The array returned by this method is not retained and can be safely
   * modified.
   *
   * @return  an array containing edge weights
   */
  public double[] materializeEdgeWeights() {
    int k = -1;
    final double[] edgeWeights = new double[nNodes];
    for (int i = 0; i < nNodes; ++i) {
      for (int j = i + 1; j < nNodes; ++j) {
        edgeWeights[++k] = getEdgeWeight(i, j);
      }
    }

    return edgeWeights;
  }

  /**
   * Returns a copy of the edge weights in a symmetric matrix of size {@code nNodes}.
   *
   * The matrix returned by this method is not retained and can be safely
   * modified.
   *
   * @return  a symmetric matrix containing edge weights
   */
  public double[][] materializeEdgeWeightsMatrix() {
    final double[][] edgeWeights = new double[nNodes][nNodes];
    for (int i = 0; i < nNodes; ++i) {
      for (int j = i + 1; j < nNodes; ++j) {
        edgeWeights[i][j] = edgeWeights[i][j] = getEdgeWeight(i, j);
      }
    }

    return edgeWeights;
  }

  // ==========================================================================
  // Concrete classes
  // ==========================================================================

  public static class ExplicitTspLibInstance extends TspInstance {
    final double[] edgeWeights;  // encodes a row-first strict upper triangular matrix

    public ExplicitTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType DataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords, final double[] edgeWeights
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, DataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
      this.edgeWeights = edgeWeights;
    }

    private ExplicitTspLibInstance(final TsplibFileData data) {
      super(data);
      this.edgeWeights = data.edges();
    }

    @Override
    public double getEdgeWeight(int i, int j) {
      final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, nNodes);
      return edgeWeights[k];
    }

    @Override
    public double[] materializeEdgeWeights() {
      return Arrays.copyOf(edgeWeights, edgeWeights.length);
    }

    @Override
    public double[][] materializeEdgeWeightsMatrix() {
      final double[][] weightsMatrix = new double[nNodes][nNodes];
      for (int i = 0; i < nNodes; ++i) {
        for (int j = i + 1; j < nNodes; ++j) {
          final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, nNodes);
          weightsMatrix[i][j] = weightsMatrix[i][j] = edgeWeights[k];
        }
      }

      return weightsMatrix;
    }
  }

  public static class Euclidean2dTspLibInstance extends TspInstance {
    public Euclidean2dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private Euclidean2dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.roundedEuclideanDistance(x[0], x[1], y[0], y[1]);
    }
  }

  public static class Euclidean3dTspLibInstance extends TspInstance {
    public Euclidean3dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private Euclidean3dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.roundedEuclideanDistance(x[0], x[1], x[2], y[0], y[1], y[2]);
    }
  }

  public static class PseudoEuclidean2dTspLibInstance extends TspInstance {
    public PseudoEuclidean2dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private PseudoEuclidean2dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.pseudoEuclideanDistance(x[0], x[1], y[0], y[1]);
    }
  }

  public static class CeilEuclidean2dTspLibInstance extends TspInstance {
    public CeilEuclidean2dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private CeilEuclidean2dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.ceilEuclideanDistance(x[0], x[1], y[0], y[1]);
    }
  }

  public static class Manhattan2dTspLibInstance extends TspInstance {
    public Manhattan2dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private Manhattan2dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.manhattanDistance(x[0], x[1], y[0], y[1]);
    }
  }

  public static class Manhattan3dTspLibInstance extends TspInstance {
    public Manhattan3dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private Manhattan3dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.manhattanDistance(x[0], x[1], x[2], y[0], y[1], y[2]);
    }
  }

  public static class Max2dTspLibInstance extends TspInstance {
    public Max2dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private Max2dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.maxDistance(x[0], x[1], y[0], y[1]);
    }
  }

  public static class Max3dTspLibInstance extends TspInstance {
    public Max3dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private Max3dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.maxDistance(x[0], x[1], x[2], y[0], y[1], y[2]);
    }
  }

  public static class Geographic2dTspLibInstance extends TspInstance {
    public Geographic2dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
    }

    private Geographic2dTspLibInstance(final TsplibFileData data) {
      super(data);
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      final double[] x = nodeCoords[i];
      final double[] y = nodeCoords[j];
      return TsplibUtil.haversineDistance(x[0], x[1], y[0], y[1]);
    }
  }

  public static class Special2dTspLibInstance extends TspInstance {
    private final BiFunction<double[], double[], Double> edgeWeightFunc;

    public Special2dTspLibInstance(
        final String name, final ProblemType type, final String comment, final int dimension,
        final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
        final EdgeFormatData edgeFormatData, final NodeCoordType nodeCoordType,
        final DisplayDataType dataDisplayType,
        final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
        final double[][] displayCoords, BiFunction<double[], double[], Double> edgeWeightFunc
    ) {
      super(
          name, type, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
          nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
      );
      this.edgeWeightFunc = edgeWeightFunc;
    }

    private Special2dTspLibInstance(
        final TsplibFileData data, final BiFunction<double[], double[], Double> edgeWeightFunc
    ) {
      super(data);
      this.edgeWeightFunc = edgeWeightFunc;
    }

    @Override
    public double getEdgeWeight(final int i, final int j) {
      return edgeWeightFunc.apply(nodeCoords[i], nodeCoords[j]);
    }
  }
}