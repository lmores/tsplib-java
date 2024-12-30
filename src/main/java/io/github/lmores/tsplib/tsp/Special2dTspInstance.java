package io.github.lmores.tsplib.tsp;

import java.util.function.BiFunction;

import io.github.lmores.tsplib.TsplibFileData;

/**
 * TSP instance with edge weights computed using an external function.
 */
public record Special2dTspInstance(
    String name,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    BiFunction<double[], double[], Integer> edgeWeightFunc
) implements TspInstance {

  protected Special2dTspInstance(
      final TsplibFileData data, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) {
    this(data.name(), data.dimension(), data.nodeCoords(), data.displayCoords(), edgeWeightFunc);
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    return edgeWeightFunc.apply(nodeCoords[i], nodeCoords[j]);
  }
}