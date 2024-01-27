/**
 * This package provides utilities to read files in
 * <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95">TSPLIB</a>
 * format.
 * <p>
 * As of version 0.0.1 only .tsp files are supported.
 * <p>
 * This package includes a copy of the Travelling Salesman Problem (TSP)
 * instances contained in the official TSPLIB archive.
 * <p>
 * Since all TSPLIB instance files for the TSP assume that the underlying graph
 * is complete, edges are not stored explicitly in memory.
 * Except for the edges themselves, the only value associated with an edge is
 * its weight.
 * TSPLIB instance files have 13 different ways to encode edge weights
 * (EXPLICIT, EUC_2D, EUC_3D, MAX_2D, MAX_3D, MAN_2D, MAN_3D, CEIL_2D, GEO,
 * ATT, XRAY1, XRAY2 and SPECIAL).
 * Of all these possibilities, only the EXPLICIT case requires the direct
 * enumeration of all weight values, whereas in all the other cases the weight
 * is computed by a specific function starting from the coordinates of the
 * nodes involved.
 * <p>
 * Since the number of nodes can be high, when the edge weights format is
 * different from EXPLICT, weight values are not stored in memory when an
 * instance is loaded; instead, they are generated on the fly each time
 * the method {@link TspInstance#getEdgeWeight} is called.
 * If you need a in memory copy of all the edge weights you can call the
 * methods {@link TspInstance#materializeEdgeWeights} or
 * {@link TspInstance#materializeEdgeWeightsMatrix}.
 *
 * @author   Lorenzo Moreschini
 * @version  %I%
 * @since    0.0.1
 */
package io.github.lmores.tsplib;
