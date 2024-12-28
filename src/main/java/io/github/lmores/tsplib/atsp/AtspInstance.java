package io.github.lmores.tsplib.atsp;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.DisplayDataType;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeDataFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;
import io.github.lmores.tsplib.TsplibFileFormat.NodeCoordType;

public interface AtspInstance {
  public String name();
  public String comment();
  public int dimension();
  public int[] depots();
  public int[][] fixedEdges();
  public double[][] nodeCoords();
  public double[][] displayCoords();
  public EdgeWeightType edgeWeightType();
  public EdgeWeightFormat edgeWeightFormat();
  public EdgeDataFormat edgeDataFormat();
  public NodeCoordType nodeCoordType();
  public DisplayDataType displayDataType();

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
  public static AtspInstance from(final TsplibFileData data) {
    final EdgeWeightType edgeWeightType = data.edgeWeightType();
    return switch(edgeWeightType) {
      case EXPLICIT -> new ExplicitAtspInstance(data);
      case ATT, CEIL_2D, EUC_2D, EUC_3D, GEO, MAN_2D, MAN_3D,
           MAX_2D, MAX_3D, SPECIAL, XRAY1, XRAY2 ->
            throw new IllegalArgumentException("Unsupported edge weight type: " + edgeWeightType);
    };
  }

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   * Since each {@link ImplicitAtspInstance} is defined on a complete graph, this method
   * simply checks that {@code 0 <= i,j < dimension} and {@code i != j}.
   *
   * @param i  the 0-based index of a node
   * @param j  the 0-based index of the other node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  public default boolean hasEdge(final int i, final int j) {
    final int dimension = dimension();
    return 0 <= i && i < dimension && 0 <= j && j < dimension && i != j;
  }

  /**
   * Returns the value of the weight associated with the unordered edge joining
   * nodes {@code i} and {@code j}.
   *
   * @param i  0-based index of one node of the edge
   * @param j  0-based index of the other node of the edge
   * @return   the weight of the edge
   */
  public abstract int getEdgeWeight(final int i, final int j);

  public default int computeTourValue(final int[] tour) {
    final int n = tour.length;
    if (n < 2)  return 0;

    int value = getEdgeWeight(tour[n-1], tour[0]);
    for (int i = 0, m = n - 1; i < m; ++i) {
      value +=  getEdgeWeight(tour[i], tour[i+1]);
    }

    return value;
  }
}
