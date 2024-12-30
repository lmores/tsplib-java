package io.github.lmores.tsplib.tsp;

import io.github.lmores.tsplib.TsplibFileData;

/**
 * TSP instance with edge weights explicitly defined.
 */
public record ExplicitTspInstance(
    String name,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] edgeWeights
) implements TspInstance {

  protected ExplicitTspInstance(final TsplibFileData data) {
    this(data.name(), data.dimension(), data.nodeCoords(), data.displayCoords(), data.edgeWeights());
  }

  @Override
  public int getEdgeWeight(int i, int j) {
    return edgeWeights[i][j];
  }
}