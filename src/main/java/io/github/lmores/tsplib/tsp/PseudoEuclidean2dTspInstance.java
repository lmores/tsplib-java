package io.github.lmores.tsplib.tsp;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;
import io.github.lmores.tsplib.TsplibUtil;

/**
 * TSP instance with edge weights computed using a pseudo euclidean distance.
 */
public record PseudoEuclidean2dTspInstance(
    String name,
    String comment,
    EdgeWeightType edgeWeightType,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] fixedEdges
) implements TspInstance {

  public static PseudoEuclidean2dTspInstance from(final TsplibFileData data) {
    return new PseudoEuclidean2dTspInstance(
        data.name(), data.comment(), data.edgeWeightType(), data.dimension(),
        data.nodeCoords(), data.displayCoords(), data.fixedEdges()
    );
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    final double[] x = nodeCoords[i];
    final double[] y = nodeCoords[j];
    return TsplibUtil.pseudoEuclideanDistance(x[0], x[1], y[0], y[1]);
  }
}