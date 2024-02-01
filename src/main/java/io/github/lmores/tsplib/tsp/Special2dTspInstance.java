package io.github.lmores.tsplib.tsp;

import java.util.function.BiFunction;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.*;

/** TSP instance with edge weights computed using an external function. */
public class Special2dTspInstance extends TspInstance {
  private final BiFunction<double[], double[], Integer> edgeWeightFunc;

  public Special2dTspInstance(
      final String name, final String comment, final int dimension,
      final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
      final EdgeDataFormat edgeFormatData, final NodeCoordType nodeCoordType,
      final DisplayDataType dataDisplayType,
      final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
      final double[][] displayCoords, BiFunction<double[], double[], Integer> edgeWeightFunc
  ) {
    super(
        name, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
        nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
    );
    this.edgeWeightFunc = edgeWeightFunc;
  }

  protected Special2dTspInstance(
      final TsplibFileData data, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) {
    super(data);
    this.edgeWeightFunc = edgeWeightFunc;
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    final double[][] nodeCoords = getNodeCoords();
    return edgeWeightFunc.apply(nodeCoords[i], nodeCoords[j]);
  }
}