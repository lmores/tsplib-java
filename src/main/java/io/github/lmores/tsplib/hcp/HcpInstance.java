package io.github.lmores.tsplib.hcp;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashSet;
import java.util.Set;

import io.github.lmores.tsplib.BaseInstance;
import io.github.lmores.tsplib.TsplibFileData;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeDataFormat;
import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

/**
 * Represent a HCP instances.
 *
 * @author  Lorenzo Moreschini
 * @since   0.0.1
 */
public record HcpInstance(
    String name,
    String comment,
    EdgeWeightType edgeWeightType,
    int dimension,
    double[][] nodeCoords,
    double[][] displayCoords,
    int[][] fixedEdges,
    Set<SimpleImmutableEntry<Integer,Integer>> edges
) implements BaseInstance {

  /**
   * Returns a HCP instance backed by the provided data.
   *
   * @param data            the instance data
   * @return                a HCP instance
   */
  public static HcpInstance from(final TsplibFileData data) {
    return new HcpInstance(
        data.name(), data.comment(), data.edgeWeightType(), data.dimension(),
        data.nodeCoords(), data.displayCoords(), data.fixedEdges(),
        buildEdgeSet(data.edges(), data.edgeDataFormat())
    );
  }

  @Override
  public int getEdgeWeight(final int i, final int j) {
    return hasEdge(i, j) ? 1 : 0;
  }

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   *
   * @param i  the 0-based index of a node
   * @param j  the 0-based index of the other node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  @Override
  public boolean hasEdge(final int i, final int j) {
    return edges.contains(new SimpleImmutableEntry<Integer,Integer>(i, j));
  }

  // ==============================================================================================
  // Private helpers
  // ==============================================================================================

  private static final Set<SimpleImmutableEntry<Integer,Integer>> buildEdgeSet(
      final int[][] edgeData, final EdgeDataFormat format
  ) {
    final Set<SimpleImmutableEntry<Integer,Integer>> edges = new HashSet<>(edgeData.length);
    switch (format) {
      case ADJ_LIST -> {
        for (final int[] adj: edgeData) {
          for (int i = 1, n = adj.length; i < n; ++i) {
            edges.add(new SimpleImmutableEntry<>(adj[0], adj[i]));
            edges.add(new SimpleImmutableEntry<>(adj[i], adj[0]));
          }
        }
      }

      case EDGE_LIST -> {
        for (final int[] e: edgeData) {
          edges.add(new SimpleImmutableEntry<>(e[0], e[1]));
          edges.add(new SimpleImmutableEntry<>(e[1], e[0]));
        }
      }

      case null, default -> {
        throw new UnsupportedOperationException("Unsupported edge data format: " + format);
      }
    }

    return edges;
  }
}
