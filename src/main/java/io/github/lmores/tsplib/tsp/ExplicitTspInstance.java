package io.github.lmores.tsplib.tsp;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

/**
 * TSP instance with edge weights explicitly defined.
 */
public record ExplicitTspInstance(
    String name,
    String comment,
    EdgeWeightType edgeWeightType,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] fixedEdges,
    int[][] edgeWeights
) implements TspInstance {

  public static ExplicitTspInstance from(final TsplibFileData data) {
    return new ExplicitTspInstance(
        data.name(), data.comment(), data.edgeWeightType(), data.dimension(),
        data.nodeCoords(), data.displayCoords(), data.fixedEdges(), data.edgeWeights()
    );
  }

  @Override
  public int getEdgeWeight(int i, int j) {
    return edgeWeights[i][j];
  }
}