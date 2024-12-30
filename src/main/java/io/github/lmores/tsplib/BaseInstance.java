package io.github.lmores.tsplib;

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
  // public abstract String comment();

  /**
   * Returns the number of nodes (including depots).
   *
   * @return the number of nodes (including depots)
   */
  public abstract int dimension();

  /**
   * Returns the nodes to be used as depots.
   *
   * @return the nodes to be used as depots
   */
  // public int[] depots();

  /**
   * Returns the edges (two dimensional arrays) that must be traversed.
   *
   * @return the edges that must be traversed
   */
  // public int[][] fixedEdges();

  /**
   * Returns the node coordinates (used to compute edge weights).
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

  /**
   * Returns the rule used to compute edge weights.
   *
   * @return the rule used to compute edge weights.
   */
  // public EdgeWeightType getEdgeWeightType() { return edgeWeightType; }

  /**
   * Returns the format used to encode edge weights.
   *
   * @return the format used to encode edge weights
   */
  // public EdgeWeightFormat getEdgeWeightFormat() { return edgeWeightFormat; }

  /**
   * Returns the format used to encode the edge of the graph.
   *
   * @return the format used to encode the edge of the graph
   */
  // public EdgeDataFormat getEdgeDataFormat() { return edgeDataFormat; }

  /**
   * Returns the type of coordinates associated with the nodes of the graph.
   *
   * @return the type of coordinates associated with the nodes of the graph.
   */
  // public NodeCoordType getNodeCoordType() { return nodeCoordType; }

  /**
   * Returns how a graphical display of the nodes can be obtained.
   *
   * @return how a graphical display of the nodes can be obtained
   */
  // public DisplayDataType getDisplayDataType() { return displayDataType; }
}
