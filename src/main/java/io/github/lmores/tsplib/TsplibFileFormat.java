package io.github.lmores.tsplib;

import java.util.regex.Pattern;

/**
 * Collection of constant values used in TSPLIB instance files.
 * See TSPLIB specifications for details.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 * @see      <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp95.pdf">
 *               TSPLIB specification
 *           </a>
 */
public class TsplibFileFormat {

  /** Matches the delimiter symbol(s) for parsing TSPLIB instance files. */
  public static final Pattern DELIMITER = Pattern.compile("[\\p{javaWhitespace}:]+");

  /** This class contains only static methods and no instance is allowed. */
  private TsplibFileFormat() { /* no-op */ }

  // ==============================================================================================
  // Specification part
  // ==============================================================================================

  /** Section marker that denotes the end of a file in TSPLIB format. */
  public final String EOF = "EOF";

  /** Values allowed in the 'TYPE' section of TSPLIB file format. */
  public enum ProblemType {
    /** Asymmetric Travelling Salesman Problem */
    ATSP,

    /** Capacitated vehivle routing problem */
    CVRP,

    /** Hamiltonian Cycle Problem */
    HCP,

    /** Sequential Ordering Problem */
    SOP,

    /** Tour, a sequence of nodes */
    TOUR,

    /** Travelling Salesman Problem */
    TSP,
  };

  /** Values allowed in the 'EDGE_WEIGHT_TYPE' section of TSPLIB file format. */
  public enum EdgeWeightType {
    /** Weights are listed explicitly in the corresponding section */
    EXPLICIT,

    /** Weights are Euclidean distances in 2D */
    EUC_2D,

    /** Weights are Euclidean distances in 3D */
    EUC_3D,

    /** Weights are maximum distances in 2D */
    MAX_2D,

    /** Weights are maximum distances in 3D */
    MAX_3D,

    /** Weights are Manhattan distances in 2D */
    MAN_2D,

    /** Weights are Manhattan distances in 3D */
    MAN_3D,

    /** Weights are Euclidean distances in 2D rounded up */
    CEIL_2D,

    /** Weights are geographical distances */
    GEO,

    /** Special distance function for problems att48 and att532 */
    ATT,

    /** Special distance function for crystallography problems (version 1) */
    XRAY1,

    /** Special distance function for crystallography problems (version 2) */
    XRAY2,

    /** There is a special distance function documented elsewhere */
    SPECIAL,
  }

  /** Values allowed in the 'EDGE_WEIGHT_FORMAT' section of TSPLIB file format. */
  public enum EdgeWeightFormat {
    /** Weights are given by a function (see above) */
    FUNCTION,

    /** Weights are given by a full matrix */
    FULL_MATRIX,

    /** Upper triangular matrix (row-wise without diagonal entries) */
    UPPER_ROW,

    /** Lower triangular matrix (row-wise without diagonal entries) */
    LOWER_ROW,

    /** Upper triangular matrix (row-wise including diagonal entries) */
    UPPER_DIAG_ROW,

    /** Lower triangular matrix (row-wise including diagonal entries) */
    LOWER_DIAG_ROW,

    /** Upper triangular matrix (column-wise without diagonal entries) */
    UPPER_COL,

    /** Lower triangular matrix (column-wise without diagonal entries) */
    LOWER_COL,

    /** Upper triangular matrix (column-wise including diagonal entries) */
    UPPER_DIAG_COL,

    /** Lower triangular matrix (column-wise including diagonal entries) */
    LOWER_DIAG_COL,
  }

  /** Values allowed in the 'EDGE_FORMAT_DATA' section of TSPLIB file format. */
  public enum EdgeDataFormat {
    /** The graph is given by an edge list */
    EDGE_LIST,

    /** The graph is given as an adjacency list */
    ADJ_LIST,
  }

  /** Values allowed in the 'NODE_COORD_TYPE' section of TSPLIB file format. */
  public enum NodeCoordType {
    /** Nodes are specified by coordinates in 2D */
    TWOD_COORDS,

    /** Nodes are specified by coordinates in 3D */
    THREED_COORDS,

    /** The nodes do not have associated coordinates */
    NO_COORDS,
  }

  /** Values allowed in the 'DISPLAY_DATA_TYPE' section of TSPLIB file format. */
  public enum DisplayDataType {
    /** Display is generated from the node coordinates */
    COORD_DISPLAY,

    /** Explicit coordinates in 2D are given */
    TWOD_DISPLAY,

    /** No graphical display is possible */
    NO_DISPLAY,
  }

  // ==============================================================================================
  // Inner classes
  // ==============================================================================================

  /**
   * Exception thrown when an error is encountered while parsing a TSPLIB file.
   *
   * @author   Lorenzo Moreschini
   * @since    0.0.1
   */
  public static class TsplibFileFormatException extends RuntimeException {

    /**
     * Runtime exception thrown when inconsistent data is found while parsing
     * a file in TSPLIB format.
     *
     * @param message  a human readable description of the error encountered
     */
    public TsplibFileFormatException(final String message) {
      super(message);
    }
  }
}
