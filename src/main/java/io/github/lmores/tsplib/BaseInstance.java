package io.github.lmores.tsplib;

import io.github.lmores.tsplib.TsplibFileFormat.EdgeWeightType;

public interface BaseInstance {

  /**
   * Returns the name of the instance.
   *
   * @return the name of the instance
   */
  public abstract String name();

  /**
   * Returns the comment associated with the instance.
   *
   * @return the comment associated with the instance
   */
  public abstract String comment();

  /**
   * Returns the number of nodes.
   *
   * @return the number of nodes
   */
  public abstract int dimension();

  /**
   * Returns the node coordinates used to compute edge weights.
   *
   * @return the node coordinates (used to compute edge weights)
   */
  public abstract double[][] nodeCoords();

  /**
   * Returns the node coordinates to be used for graphical display only.
   *
   * @return the node coordinates to be used for graphical display only.
   */
  public abstract double[][] displayCoords();

  /**
   * Returns the rule used to compute edge weights.
   *
   * @return the rule used to compute edge weights.
   */
  public abstract EdgeWeightType edgeWeightType();

  /**
   * Returns the edges (as two dimensional arrays) that must be traversed.
   *
   * @return the edges that must be traversed
   */
  public abstract int[][] fixedEdges();

  /**
   * Returns the weight of the edge that joins node {@code i} and {@code j}.
   *
   * @param i  0-based index of one node of the edge
   * @param j  0-based index of the other node of the edge
   * @return   the weight of the edge
   */
  public abstract int getEdgeWeight(final int i, final int j);

  /**
   * Checks whether there esists an edge joining nodes {@code i} and {@code j}.
   *
   * @param i  the 0-based index of a node
   * @param j  the 0-based index of the other node
   * @return   true if an edge joins nodes {@code i} and {@code j}, false otherwise
   */
  public abstract boolean hasEdge(final int i, final int j);

  /**
   * Compute the value of a tour (i.e. the sum of the weights of its edges).
   *
   * The tour to evaluate must be provided as a sequence of nodes (with 0-based
   * indexes) without repeating the first-last node; e.g. the array
   * {@code {2,3,5,7}} represents the tour 2 -> 3 -> 5 -> 7 -> 2.
   *
   * @param tour  the sequence of nodes (with 0-based indexes) defining the tour
   * @return      the value of the tour
   */
  public default int computeTourValue(final int[] tour) {
    final int n = tour.length;
    if (n < 2)  return 0;

    int value = getEdgeWeight(tour[n-1], tour[0]);
    for (int i = 0, m = n - 1; i < m; ++i) {
      value +=  getEdgeWeight(tour[i], tour[i+1]);
    }

    return value;
  }

  /**
   * Returns a copy of the edge weights in a square matrix of size
   * {@code dimension}. The returned matrix is not retained.
   *
   * @return  a square matrix containing edge weights
   */
  public default int[][] materializeEdgeWeightsMatrix() {
    final int dimension = dimension();
    final int[][] weights = new int[dimension][dimension];
    for (int i = 0; i < dimension; ++i) {
      for (int j = 0; j < dimension; ++j) {
        weights[i][j] = getEdgeWeight(i, j);
      }
    }

    return weights;
  }
}
