package io.github.lmores.tsplib.tsp;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibUtil;
import io.github.lmores.tsplib.TsplibFileFormat.*;

/** TSP instance with edge weights computed using the haversine distance. */
public class Geographic2dTspInstance extends TspInstance {
  public Geographic2dTspInstance(
      final String name, final String comment, final int dimension,
      final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
      final EdgeDataFormat edgeFormatData, final NodeCoordType nodeCoordType,
      final DisplayDataType dataDisplayType,
      final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
      final double[][] displayCoords
  ) {
    super(
        name, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
        nodeCoordType, dataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
    );
  }

  protected Geographic2dTspInstance(final TsplibFileData data) {
    super(data);
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    final double[][] nodeCoords = getNodeCoords();
    final double[] x = nodeCoords[i];
    final double[] y = nodeCoords[j];
    return TsplibUtil.roundedHaversineDistance(x[0], x[1], y[0], y[1]);
  }
}