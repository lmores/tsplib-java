package io.github.lmores.tsplib.atsp;

import io.github.lmores.tsplib.TsplibFileData;

/**
 * ATSP instance with edge weights explicitly defined.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public record ExplicitAtspInstance(
    String name,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] edgeWeights
) implements AtspInstance {

  protected ExplicitAtspInstance(final TsplibFileData data) {
    this(data.name(), data.dimension(), data.nodeCoords(), data.displayCoords(), data.edgeWeights());
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    return edgeWeights[i][j];
  }
}
