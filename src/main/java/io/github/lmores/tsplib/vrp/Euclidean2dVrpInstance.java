package io.github.lmores.tsplib.vrp;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibUtil;

/**
 * VRP instance with edge weights computed using the 2D euclidean distance.
 */
public record Euclidean2dVrpInstance(
    String name,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords
) implements VrpInstance {

  protected Euclidean2dVrpInstance(final TsplibFileData data) {
    this(data.name(), data.dimension(), data.nodeCoords(), data.displayCoords());
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    final double[] x = nodeCoords[i];
    final double[] y = nodeCoords[j];
    return TsplibUtil.roundedEuclideanDistance(x[0], x[1], y[0], y[1]);
  }
}