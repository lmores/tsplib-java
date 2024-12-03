package io.github.lmores.tsplib.atsp;

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
}
