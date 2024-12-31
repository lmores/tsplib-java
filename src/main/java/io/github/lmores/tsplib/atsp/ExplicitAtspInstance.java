package io.github.lmores.tsplib.atsp;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

/**
 * ATSP instance with edge weights explicitly defined.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public record ExplicitAtspInstance(
    String name,
    String comment,
    EdgeWeightType edgeWeightType,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] fixedEdges,
    int[][] edgeWeights
) implements AtspInstance {

  public static ExplicitAtspInstance from(final TsplibFileData data) {
    return new ExplicitAtspInstance(
        data.name(), data.comment(), data.edgeWeightType(), data.dimension(),
        data.nodeCoords(), data.displayCoords(), data.fixedEdges(), data.edgeWeights()
    );
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    return edgeWeights[i][j];
  }
}
