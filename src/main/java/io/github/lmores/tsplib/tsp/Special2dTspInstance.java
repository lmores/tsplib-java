package io.github.lmores.tsplib.tsp;

import java.util.function.BiFunction;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

/**
 * TSP instance with edge weights computed using an external function.
 */
public record Special2dTspInstance(
    String name,
    String comment,
    EdgeWeightType edgeWeightType,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] fixedEdges,
    BiFunction<double[], double[], Integer> edgeWeightFunc
) implements TspInstance {

  public static Special2dTspInstance from(
      final TsplibFileData data, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) {
    return new Special2dTspInstance(
        data.name(), data.comment(), data.edgeWeightType(), data.dimension(),
        data.nodeCoords(), data.displayCoords(), data.fixedEdges(), edgeWeightFunc
    );
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    return edgeWeightFunc.apply(nodeCoords[i], nodeCoords[j]);
  }
}