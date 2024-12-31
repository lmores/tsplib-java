package io.github.lmores.tsplib.vrp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiFunction;

import io.github.lmores.tsplib.BaseInstance;
import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

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
public interface VrpInstance extends BaseInstance {

  /**
   * Returns a VRP instance backed by the provided data.
   *
   * @param data  the instance data
   * @return      a VRP instance
   */
  public static VrpInstance from(final TsplibFileData data) {
    return from(data, null);
  }

  /**
   * Returns a  instance backed by the provided data using a user-defined
   * function to compute edge weights.
   * <p>
   * The edge weight function accepts two arrays of double values and returns
   * the weight value as a double.
   * To compute the weight of the edge joining nodes {@code i} and {@code j}
   * the provided function is called using as argument the coordinates of nodes
   * {@code i} and {@code j}.
   *
   * @param data            the instance data
   * @param edgeWeightFunc  a user-defined function to compute edge weights
   * @return                a TSP instance
   */
  public static VrpInstance from(
      final TsplibFileData data, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) {
    final EdgeWeightType edgeWeightType = data.edgeWeightType();
    if (edgeWeightType == EdgeWeightType.SPECIAL && edgeWeightFunc == null) {
      throw new IllegalArgumentException(
          "Instance '" + data.name() + "': edge weight format is 'SPECIAL', " +
          "but the user-defined function is null"
      );
    }

    return switch(edgeWeightType) {
      case EXPLICIT -> new ExplicitVrpInstance(data);
      case ATT -> new PseudoEuclidean2dVrpInstance(data);
      case EUC_2D -> new Euclidean2dVrpInstance(data);
      case CEIL_2D, EUC_3D, GEO, MAN_2D, MAN_3D, MAX_2D, MAX_3D, SPECIAL, XRAY1, XRAY2 ->
          throw new UnsupportedOperationException("Unimplemented edge weight type: " + edgeWeightType);
      default -> throw new IllegalArgumentException("Unexpected edge weight type: " + edgeWeightType);
    };
  }

  /**
   * Loads a VRP instance from a file in TSPLIB format.
   *
   * @param file  the file containing the instance data
   * @return      the VRP instance
   * @throws IOException  if a I/O error occurs
   */
  public static VrpInstance read(final Path file) throws IOException {
    return read(file, null);
  }

  /**
   * Loads a VRP instance from a file in TSPLIB format using the provided
   * function to compute edge weights if the edge weight format is 'SPECIAL'.
   *
   * @param file            the file containing the instance data
   * @param edgeWeightFunc  a custom function to compute edge weights
   * @return                the TSP instance
   * @throws IOException    if a I/O error occurs
   */
  public static VrpInstance read(
      final Path file, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) throws IOException {
    return VrpInstance.from(TsplibFileData.read(file), edgeWeightFunc);
  }

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   * Since each {@link VrpInstance} is defined on a complete graph, this method
   * simply checks that {@code 0 <= i,j < dimension} and {@code i != j}.
   *
   * @param i  the 0-based index of a node
   * @param j  the 0-based index of the other node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  public default boolean hasEdge(final int i, final int j) {
    return 0 <= i && i < dimension() && 0 <= j && j < dimension() && i != j;
  }
}
