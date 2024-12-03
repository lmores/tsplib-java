package io.github.lmores.tsplib.atsp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiFunction;

import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat;
import io.github.lmores.tsplib.TsplibFileFormat.DisplayDataType;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeDataFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;
import io.github.lmores.tsplib.TsplibFileFormat.NodeCoordType;

/**
 * Common methods for all implementations of TSP instances.
 * Subclasses differs only in the strategy adopted to compute edge weights.
 * <p>
 * The following assumptions hold for any concrete implementation of this class:
 * <ol>
 *    <li>Nodes are represented using 0-based indexes
 *        (from {@code 0} to {@code dimension - 1} inclusive)
 *    </li>
 *    <li>Graphs are always complete, hence there always exists an edge joining
 *        node {@code i} and node {@code j} for each
 *        {@code 0 <= i,j <= dimension - 1} and {@code i != j}.
 *    </li>
 * </ol>
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public record ImplicitAtspInstance(
    String name,
    String comment,
    int dimension,
    int[] depots,
    int[][] fixedEdges,
    double[][] nodeCoords,
    double[][] displayCoords,
    EdgeWeightType edgeWeightType,
    EdgeWeightFormat edgeWeightFormat,
    EdgeDataFormat edgeDataFormat,
    NodeCoordType nodeCoordType,
    DisplayDataType displayDataType,
    BiFunction<Integer, Integer, Integer> edgeWeightFunc
) implements AtspInstance {

  /**
   * Returns the value of the weight associated with the unordered edge joining
   * nodes {@code i} and {@code j}.
   *
   * @param i  0-based index of one node of the edge
   * @param j  0-based index of the other node of the edge
   * @return   the weight of the edge
   */
  public int getEdgeWeight(final int i, final int j) {
    return edgeWeightFunc.apply(i, j);
  }

  /**
   * Returns a copy of the edge weights as an array representing the strict
   * upper diagonal of a matrix of size {@code dimension}.
   * The returned array is not retained.
   *
   * @return  an array containing edge weights
   */
  public int[] materializeEdgeWeights() {
    int k = -1;
    final int[] edgeWeights = new int[dimension * dimension];
    for (int i = 0; i < dimension; ++i) {
      for (int j = 0; j < dimension; ++j) {
        edgeWeights[++k] = getEdgeWeight(i, j);
      }
    }

    return edgeWeights;
  }

  /**
   * Returns a copy of the edge weights as a symmetric matrix of size
   * {@code dimension}. The returned matrix is not retained.
   *
   * @return  a symmetric matrix containing edge weights
   */
  public int[][] materializeEdgeWeightsMatrix() {
    final int[][] edgeWeights = new int[dimension][dimension];
    for (int i = 0; i < dimension; ++i) {
      final int[] row = edgeWeights[i];
      for (int j = 0; j < dimension; ++j) {
        row[j] = getEdgeWeight(i, j);
      }
    }

    return edgeWeights;
  }
}
