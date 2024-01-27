package io.github.lmores.tsplib;

import java.util.regex.Pattern;

/**
 * Collection of constant values used in TSPLIB instance files.
 * See TSPLIB specifications for details.
 *
 * @author   Lorenzo Moreschini
 * @version  %I%
 * @since    0.0.1
 * @see      <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp95.pdf">
 *               TSPLIB specification
 *           </a>
 */
public class TsplibFileFormat {
  /** Regular expression used to match the delimiter symbol(s) when parsing instance files. */
  public static final Pattern DELIMITER = Pattern.compile("[\\p{javaWhitespace}:]+");

  /** This class contains only static methods and no instance is allowed. */
  private TsplibFileFormat() { /* no-op */ }

  // ==============================================================================================
  // Specification part
  // ==============================================================================================

  public enum ProblemType { TSP, ATSP, SOP, HCP, CVRP, TOUR };

  public enum EdgeWeightType {
    EXPLICIT,  // Weights are listed explicitly in the corresponding section
    EUC_2D,    // Weights are Euclidean distances in 2D
    EUC_3D,    // Weights are Euclidean distances in 3D
    MAX_2D,    // Weights are maximum distances in 2D
    MAX_3D,    // Weights are maximum distances in 3D
    MAN_2D,    // Weights are Manhattan distances in 2D
    MAN_3D,    // Weights are Manhattan distances in 3D
    CEIL_2D,   // Weights are Euclidean distances in 2D rounded up
    GEO,       // Weights are geographical distances
    ATT,       // Special distance function for problems att48 and att532
    XRAY1,     // Special distance function for crystallography problems (version 1)
    XRAY2,     // Special distance function for crystallography problems (version 2)
    SPECIAL,   // There is a special distance function documented elsewhere
  }

  public enum EdgeWeightFormat {
    FUNCTION,        // Weights are given by a function (see above)
    FULL_MATRIX,     // Weights are given by a full matrix
    UPPER_ROW,       // Upper triangular matrix (row-wise without diagonal entries)
    LOWER_ROW,       // Lower triangular matrix (row-wise without diagonal entries)
    UPPER_DIAG_ROW,  // Upper triangular matrix (row-wise including diagonal entries)
    LOWER_DIAG_ROW,  // Lower triangular matrix (row-wise including diagonal entries)
    UPPER_COL,       // Upper triangular matrix (column-wise without diagonal entries)
    LOWER_COL,       // Lower triangular matrix (column-wise without diagonal entries)
    UPPER_DIAG_COL,  // Upper triangular matrix (column-wise including diagonal entries)
    LOWER_DIAG_COL,  // Lower triangular matrix (column-wise including diagonal entries)
  }

  public enum EdgeFormatData {
    EDGE_LIST,  // The graph is given by an edge list
    ADJ_LIST,   // The graph is given as an adjacency list
  }

  public enum NodeCoordType {
    TWOD_COORDS,    // Nodes are specified by coordinates in 2-D
    THREED_COORDS,  // Nodes are specified by coordinates in 3-D
    NO_COORDS,      // The nodes do not have associated coordinates
  }

  public enum DisplayDataType {
    COORD_DISPLAY,  // Display is generated from the node coordinates
    TWOD_DISPLAY,   // Explicit coordinates in 2-D are given
    NO_DISPLAY,     // No graphical display is possible
  }

  public final String EOF = "EOF";

  // ==============================================================================================
  // Inner classes
  // ==============================================================================================

  public static class TsplibFileFormatException extends RuntimeException {
    public TsplibFileFormatException(final String message) {
      super(message);
    }
  }
}
