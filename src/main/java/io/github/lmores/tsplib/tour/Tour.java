package io.github.lmores.tsplib.tour;

import io.github.lmores.tsplib.TsplibFileData;

/**
 * Representation of a tour on a graph as a sequence of nodes.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public record Tour (String name, String comment, int dimension, int[][] tours) {
  /**
   * Returns a tour instance backed by the provided data.
   *
   * @param data  the tour data
   * @return      a tour instance
   */
  public static Tour from(final TsplibFileData data) {
    return new Tour(data.name(), data.comment(), data.dimension(), data.tours());
  }
}
