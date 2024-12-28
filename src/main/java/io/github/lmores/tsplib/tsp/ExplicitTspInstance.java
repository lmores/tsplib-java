package io.github.lmores.tsplib.tsp;

import io.github.lmores.tsplib.TsplibFileData;

import static io.github.lmores.tsplib.TsplibFileFormat.*;

/** TSP instance with edge weights explicitly defined. */
public class ExplicitTspInstance extends TspInstance {
  final int[][] edgeWeights;

  public ExplicitTspInstance(
      final String name, final String comment, final int dimension,
      final EdgeWeightType edgeWeightType, final EdgeWeightFormat edgeWeightFormat,
      final EdgeDataFormat edgeFormatData, final NodeCoordType nodeCoordType,
      final DisplayDataType DataDisplayType,
      final double[][] nodeCoords, final int[] depots, final int[][] fixedEdges,
      final double[][] displayCoords, final int[][] edgeWeights
  ) {
    super(
        name, comment, dimension, edgeWeightType, edgeWeightFormat, edgeFormatData,
        nodeCoordType, DataDisplayType, nodeCoords, depots, fixedEdges, displayCoords
    );
    this.edgeWeights = edgeWeights;
  }

  protected ExplicitTspInstance(final TsplibFileData data) {
    super(data);
    this.edgeWeights = data.edgeWeights();
  }

  @Override
  public int getEdgeWeight(int i, int j) {
    return edgeWeights[i][j];
  }

  @Override
  public int[] materializeEdgeWeights() {
    return null;
  }

  @Override
  public int[][] materializeEdgeWeightsMatrix() {
    final int nNodes = getDimension();
    final int[][] weightsMatrix = new int[nNodes][nNodes];
    for (int i = 0; i < nNodes; ++i) {
      System.arraycopy(edgeWeights[i], 0, weightsMatrix[i], 0, nNodes);
    }

    return weightsMatrix;
  }
}