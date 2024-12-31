package io.github.lmores.tsplib.atsp;

import io.github.lmores.tsplib.BaseInstance;
import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

public interface AtspInstance extends BaseInstance {
  /**
   * Returns an ATSP instance backed by the provided data.
   *
   * Currently, only {@code TsplibFileData} objects containing explicit edge weights are supported.
   *
   * @param data            the instance data
   * @return                an ATSP instance
   */
  public static AtspInstance from(final TsplibFileData data) {
    final EdgeWeightType edgeWeightType = data.edgeWeightType();
    return switch(edgeWeightType) {
      case EXPLICIT -> ExplicitAtspInstance.from(data);
      case ATT, CEIL_2D, EUC_2D, EUC_3D, GEO, MAN_2D, MAN_3D, MAX_2D, MAX_3D, SPECIAL, XRAY1, XRAY2 -> {
        throw new IllegalArgumentException("Unimplemented edge weight type: " + edgeWeightType);
      }
    };
  }

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   * Since each {@link AtspInstance} is defined on a complete graph, this method
   * simply checks that {@code 0 <= i,j < dimension} and {@code i != j}.
   *
   * @param i  the 0-based index of a node
   * @param j  the 0-based index of the other node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  public default boolean hasEdge(final int i, final int j) {
    final int dimension = dimension();
    return 0 <= i && i < dimension && 0 <= j && j < dimension && i != j;
  }
}
