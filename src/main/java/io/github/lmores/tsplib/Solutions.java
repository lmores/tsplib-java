package io.github.lmores.tsplib;

/**
 * Representation of a tour on a graph as a sequence of nodes.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public record Solutions(String name, String comment, int dimension, int[][] tours) {

  /**
   * Returns an instance backed by the provided data.
   *
   * @param data  the tour(s) data
   * @return      an instance
   */
  public static Solutions from(final TsplibFileData data) {
    return new Solutions(data.name(), data.comment(), data.dimension(), data.tours());
  }
}
