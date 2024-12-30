package io.github.lmores.tsplib.tsp;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibUtil;

/**
 * TSP instance with edge weights computed using the haversine distance.
 */
public record Geographic2dTspInstance(
    String name,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords
) implements TspInstance {

  protected Geographic2dTspInstance(final TsplibFileData data) {
    this(data.name(), data.dimension(), data.nodeCoords(), data.displayCoords());
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    final double[] x = nodeCoords[i];
    final double[] y = nodeCoords[j];
    return TsplibUtil.roundedHaversineDistance(x[0], x[1], y[0], y[1]);
  }
}