package io.github.lmores.tsplib.vrp;

import io.github.lmores.tsplib.TsplibFileData;

public record ExplicitVrpInstance(
    String name,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] edgeWeights,
    int[] demands,
    int[] depots
) implements VrpInstance {

  /**
   * Builds an VRP instance backed by the provided data.
   *
   * @param data  the instance data
   * @return      a VRP instance
   */
  public ExplicitVrpInstance(final TsplibFileData data) {
    this(
        data.name(), data.dimension(), data.nodeCoords(), data.displayCoords(),
        data.edgeWeights(), data.demands(), data.depots()
    );
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    return edgeWeights[i][j];
  }

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   * Since each {@link ExplicitVrpInstance} is defined on a complete graph, this method
   * simply checks that {@code 0 <= i,j < dimension} and {@code i != j}.
   *
   * @param i  the 0-based index of a node
   * @param j  the 0-based index of the other node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  @Override
  public boolean hasEdge(final int i, final int j) {
    final int dimension = dimension();
    return 0 <= i && i < dimension && 0 <= j && j < dimension && i != j;
  }
}