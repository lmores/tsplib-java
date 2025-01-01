/**
 * This package provides utilities to read files in
 * <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95">TSPLIB</a>
 * format.
 * <p>
 * All files in TSPLIB format are supported, except those with extension
 * {@code .problems}.
 * <p>
 * This package includes a copy of the TSPLIB archive taken from the official
 * website.
 * <p>
 * As many types of problems in the TSPLIB assume that the underlying graph
 * is complete, edges are not explicitly stored in memory when possible.
 * In these cases, the only value associated with an edge is its weight.
 * TSPLIB instance files have 13 different ways to encode edge weights
 * (EXPLICIT, EUC_2D, EUC_3D, MAX_2D, MAX_3D, MAN_2D, MAN_3D, CEIL_2D, GEO,
 * ATT, XRAY1, XRAY2 and SPECIAL).
 * Out of all these possibilities, only the EXPLICIT one requires the direct
 * enumeration of all weight values, whereas in all the other cases the weight
 * is computed by a specific function using from the coordinates of the nodes.
 * <p>
 * Since the number of nodes can be high, when the edge weights format is
 * different from EXPLICT, weight values are not stored in memory when an
 * instance is loaded; instead, they are generated on the fly each time
 * the method {@link io.github.lmores.tsplib.tsp.TspInstance#getEdgeWeight}
 * is called.
 * You can obtain an in memory copy of all the edge weights using the method
 * {@link io.github.lmores.tsplib.tsp.TspInstance#materializeEdgeWeightsMatrix}.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
package io.github.lmores.tsplib;
