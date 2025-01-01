package io.github.lmores.tsplib.tsp;

import java.io.IOException;
import java.nio.file.Path;
import java.util.function.BiFunction;

import io.github.lmores.tsplib.BaseInstance;
import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

/**
 * Interface sahred by all implementations of TSP instances.
 *
 * Implementing classes differs only in the strategy used to compute edge weights.
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
public interface TspInstance extends BaseInstance {

  /**
   * Returns a TSP instance backed by the provided data.
   *
   * @param data  the instance data
   * @return      a TSP instance
   */
  public static TspInstance from(final TsplibFileData data) {
    return from(data, null);
  }

  /**
   * Returns a TSP instance backed by the provided data.
   *
   * The edge weight function is required only when
   * {@code data.edgeWeightType() == EdgeWeightType.SPECIAL}, accepts two
   * arrays of double values and returns the weight value as a double.
   *
   * To compute the weight of the edge joining nodes {@code i} and {@code j}
   * the provided function is called using as argument the coordinates of nodes
   * {@code i} and {@code j}.
   *
   * @param data            the instance data
   * @param edgeWeightFunc  a user-defined function to compute edge weights
   * @return                a TSP instance
   */
  public static TspInstance from(
      final TsplibFileData data, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) {
    final EdgeWeightType edgeWeightType = data.edgeWeightType();
    return switch(edgeWeightType) {
      case EXPLICIT -> ExplicitTspInstance.from(data);
      case ATT -> PseudoEuclidean2dTspInstance.from(data);
      case CEIL_2D -> CeilEuclidean2dTspInstance.from(data);
      case EUC_2D -> Euclidean2dTspInstance.from(data);
      case EUC_3D -> Euclidean3dTspInstance.from(data);
      case GEO -> Geographic2dTspInstance.from(data);
      case MAN_2D -> Manhattan2dTspInstance.from(data);
      case MAN_3D -> Manhattan3dTspInstance.from(data);
      case MAX_2D -> Max2dTspInstance.from(data);
      case MAX_3D -> Max3dTspInstance.from(data);
      case SPECIAL -> Special2dTspInstance.from(data, edgeWeightFunc);
      case XRAY1, XRAY2 -> throw new UnsupportedOperationException(
          "Unimplemented edge weight type: " + edgeWeightType
      );
      default -> throw new IllegalArgumentException(
          "Unsupported edge weight type: " + edgeWeightType
      );
    };
  }

  /**
   * Loads a TSP instance from a file in TSPLIB format.
   *
   * @param file  the file containing the instance data
   * @return      the TSP instance
   * @throws IOException  if a I/O error occurs
   */
  public static TspInstance read(final Path file) throws IOException {
    return read(file, null);
  }

  /**
   * Loads a TSP instance from a file in TSPLIB format using the provided
   * function to compute edge weights if the edge weight format is 'SPECIAL'.
   *
   * @param file            the file containing the instance data
   * @param edgeWeightFunc  a custom function to compute edge weights
   * @return                the TSP instance
   * @throws IOException    if a I/O error occurs
   */
  public static TspInstance read(
      final Path file, final BiFunction<double[], double[], Integer> edgeWeightFunc
  ) throws IOException {
    return TspInstance.from(TsplibFileData.read(file), edgeWeightFunc);
  }

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   * Since each {@link TspInstance} is defined on a complete graph, this method
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
