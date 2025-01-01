package io.github.lmores.tsplib.sop;

import io.github.lmores.tsplib.BaseInstance;
import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

/**
 * Represent a SOP instances.
 *
 * @author  Lorenzo Moreschini
 * @since   0.0.1
 */
public record SopInstance(
    String name,
    String comment,
    EdgeWeightType edgeWeightType,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] fixedEdges,
    int[][] edgeWeights
) implements BaseInstance {

  /**
   * Returns an SOP instance backed by the provided data.
   *
   * Currently, only {@code TsplibFileData} objects containing explicit edge weights are supported.
   *
   * @param data            the instance data
   * @return                an SOP instance
   */
  public static SopInstance from(final TsplibFileData data) {
    return new SopInstance(
        data.name(), data.comment(), data.edgeWeightType(), data.dimension(),
        data.nodeCoords(), data.displayCoords(), data.fixedEdges(), data.edgeWeights()
    );
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    return edgeWeights[i][j];
  }

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   * Since each {@link SopInstance} is defined on a complete graph, this method
   * simply checks that {@code 0 <= i,j < dimension} and {@code i != j}.
   *
   * @param i  the 0-based index of a node
   * @param j  the 0-based index of the other node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  public boolean hasEdge(final int i, final int j) { // TODO
    final int dimension = dimension();
    return 0 <= i && i < dimension && 0 <= j && j < dimension && i != j;
  }
}
