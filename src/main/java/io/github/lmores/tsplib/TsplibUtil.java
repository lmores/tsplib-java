package io.github.lmores.tsplib;

/**
 * Utilities to interact with the data contained in TSPLIB instances.
 *
 * @author   Lorenzo Moreschini
 * @since    0.0.1
 */
public class TsplibUtil {
  /** Earth radius used in the {@link roundedHaversineDistance} method. */
  public static final double EARTH_RADIUS = 6378.388;

  /** Value of pi used in the {@link roundedHaversineDistance} method. */
  public static final double PI = 3.141592;

  /** This class contains only static methods and no instance is allowed. */
  private TsplibUtil() { /* no-op */ }

  /**
   * Given the index {@code k} of an array encoding a strict upper diagonal
   * matrix of dimension {@code n}, this method returns the matrix row and
   * column indexes (0-based) corresponding to the same entry.
   *
   * Since the strict upper diagonal part of a matrix of dimension {@code n}
   * contains {@code n(n-1)/2} elements, this is also the length assumed for
   * the array encoding it, hence it must hold {@code 0 <= k < n(n-1)/2}.
   * If such requirement is not satisfied, the behaviour is undefined.
   *
   * @param k  the index of the element in the array
   * @param n  the dimension of the matrix encoded in the array
   * @return   an array of length 2 containing the row and column indexes
   */
  public static int[] arrayToStrictUpperTriangularMatrixIndexes(final int k, final int n) {
    if (k < n - 1)  return new int[] {0, k + 1};

    final int i = (int) Math.floor(n - 0.5 - Math.sqrt(Math.pow(2 * n - 1, 2) - 8 * k) / 2);
    final int I = n * i - i * (i + 1) / 2;
    return new int[] {i, i + 1 + (k - I)};
  }

  /**
   * Given the row and column indexes {@code i,j} of a strict upper diagonal
   * matrix of dimension {@code n}, this method returns the index {@code k}
   * corresponding to the same element in an array of length {@code n(n-1)/2}
   * that encodes in row-wise order the strict upper diagonal part of the
   * same matrix.
   *
   * Since the strict upper diagonal part of a matrix of dimension {@code n}
   * contains {@code n(n-1)/2} elements, this method returns integer values
   * between {@code 0} and {@code n(n-1)/2 - 1} included.
   *
   * The row and column indexes provided as input are supposed to be values
   * between {@code 0} (included) and {@code n(n-1)/2} (excluded).
   * When such requirement is not satisfied, the returned value is undefined.
   *
   * @param i  the 0-based index of the matrix row
   * @param j  the 0-based index of the matrix column
   * @param n  the dimension of the matrix
   * @return   the index corresponding to the same element inside an array that
   *           encodes in row-wise order the strict upper diagonal part of the
   *           same matrix
   */
  public static int strictUpperTriangularToArrayIndex(final int i, final int j, final int n) {
    // [n + (n-1) + ... + (n-i) - n] + (j-i-1) = [n(n-1) / 2 - (n-i)(n-i-1) / 2] + (j-i-1)
    return n * (n-1) / 2 - (n-i) * (n-i-1) / 2 + (j-i-1);
  }

  /**
   * Returns the euclidean distance between two points with coordinates
   * {@code x1, y1} and {@code x2, y2} rounded to the closest integer.
   *
   * @param x1  the x-coordinate of the first point
   * @param y1  the y-coordinate of the first point
   * @param x2  the x-coordinate of the second point
   * @param y2  the y-coordinate of the second point
   * @return    the euclidean distance rounded to the closest integer
   */
  public static int roundedEuclideanDistance(
      final double x1, final double y1, final double x2, final double y2
  ) {
    final double dx = x1 - x2;
    final double dy = y1 - y2;
    final double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    return nint(dist);
  }

  /**
   * Returns the euclidean distance between two points with coordinates
   * {@code x1, y1, z1} and {@code x2, y2, z2} rounded to the closest integer.
   *
   * @param x1  the x-coordinate of the first point
   * @param y1  the y-coordinate of the first point
   * @param z1  the z-coordinate of the first point
   * @param x2  the x-coordinate of the second point
   * @param y2  the y-coordinate of the second point
   * @param z2  the z-coordinate of the second point
   * @return    the euclidean distance rounded to the closest integer
   */
  public static int roundedEuclideanDistance(
      final double x1, final double y1, final double z1,
      final double x2, final double y2, final double z2
  ) {
    final double dx = x1 - x2;
    final double dy = y1 - y2;
    final double dz = z1 - z2;
    final double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2) + Math.pow(dz, 2));
    return nint(dist);
  }

  /**
   * Returns a pseudo euclidean distance between two points with coordinates
   * {@code x1, y1} and {@code x2, y2} rounded to the next integer.
   *
   * @param x1  the x-coordinate of the first point
   * @param y1  the y-coordinate of the first point
   * @param x2  the x-coordinate of the second point
   * @param y2  the y-coordinate of the second point
   * @return    the pseudo euclidean distance
   */
  public static int pseudoEuclideanDistance(
      final double x1, final double y1, final double x2, final double y2
  ) {
    final double dx = x1 - x2;
    final double dy = y1 - y2;
    final double r = Math.sqrt((dx*dx + dy*dy) / 10.0);
    final int t = nint(r);
    return (t < r) ? t + 1 : t;
  }

  /**
   * Returns the euclidean distance between two points with coordinates
   * {@code x1, y1} and {@code x2, y2} rounded to the next integer.
   *
   * @param x1  the x-coordinate of the first point
   * @param y1  the y-coordinate of the first point
   * @param x2  the x-coordinate of the second point
   * @param y2  the y-coordinate of the second point
   * @return int
   */
  public static int ceilEuclideanDistance(
      final double x1, final double y1, final double x2, final double y2
  ) {
    final double dx = x1 - x2;
    final double dy = y1 - y2;
    final double dist = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
    return (int) Math.ceil(dist);
  }

  /**
   * Returns the manhattan (L1) distance between two points with coordinates
   * {@code x1, y1} and {@code x2, y2} rounded to the nearest integer.
   *
   * @param x1  the x-coordinate of the first point
   * @param y1  the y-coordinate of the first point
   * @param x2  the x-coordinate of the second point
   * @param y2  the y-coordinate of the second point
   * @return    the manhattan (L1) distance rounded to the nearest integer
   */
  public static int roundedManhattanDistance(
      final double x1, final double y1, final double x2, final double y2
  ) {
    final double dx = Math.abs(x1 - x2);
    final double dy = Math.abs(y1 - y2);
    final double dist = dx + dy;
    return nint(dist);
  }

  /**
   * Returns the manhattan (L1) distance between two points with coordinates
   * {@code x1, y1, z1} and {@code x2, y2, z2} rounded to the nearest integer.
   *
   * @param x1  the x-coordinate of the first point
   * @param y1  the y-coordinate of the first point
   * @param z1  the z-coordinate of the first point
   * @param x2  the x-coordinate of the second point
   * @param y2  the y-coordinate of the second point
   * @param z2  the z-coordinate of the second point
   * @return    the manhattan (L1) distance rounded to the nearest integer
   */
  public static int roundedManhattanDistance(
      final double x1, final double y1, final double z1,
      final double x2, final double y2, final double z2
  ) {
    final double dx = Math.abs(x1 - x2);
    final double dy = Math.abs(y1 - y2);
    final double dz = Math.abs(z1 - z2);
    final double d = dx + dy + dz;
    return nint(d);
  }

  /**
   * Returns the Chebyshev (L&infin;) distance between two points with coordinates
   * {@code x1, y1} and {@code x2, y2} rounded to the nearest integer.
   *
   * @param x1  the x-coordinate of the first point
   * @param y1  the y-coordinate of the first point
   * @param x2  the x-coordinate of the second point
   * @param y2  the y-coordinate of the second point
   * @return    the Chebyshev (L&infin;) distance rounded to the nearest integer
   */
  public static int roundedChebyshevDistance(
      final double x1, final double y1, final double x2, final double y2
  ) {
    final double dx = Math.abs(x1 - x2);
    final double dy = Math.abs(y1 - y2);
    return Math.max(nint(dx), nint(dy));
  }

  /**
   * Returns the Chebyshev (L&infin;) distance between two points with coordinates
   * {@code x1, y1, z1} and {@code x2, y2, z2} rounded to the nearest integer.
   *
   * @param x1  the x-coordinate of the first point
   * @param y1  the y-coordinate of the first point
   * @param z1  the z-coordinate of the first point
   * @param x2  the x-coordinate of the second point
   * @param y2  the y-coordinate of the second point
   * @param z2  the z-coordinate of the second point
   * @return    the Chebyshev (L&infin;) distance rounded to the nearest integer
   */
  public static int roundedChebyshevDistance(
      final double x1, final double y1, final double z1,
      final double x2, final double y2, final double z2
  ) {
    final double dx = Math.abs(x1 - x2);
    final double dy = Math.abs(y1 - y2);
    final double dz = Math.abs(z1 - z2);
    return Math.max(nint(dx), Math.max(nint(dy), nint(dz)));
  }

  /**
   * Returns the distance between two points on the earth given by
   * {@code x1, y1} and {@code x2, y2} rounded to the nearest integer.
   * Each coordinate encodes the width of an angle: the integer part represents
   * the number of degrees and the decimal part represents the minutes.
   *
   * For more information about the input format and the returned value of this
   * function see the official documentation of the TSPLIB.
   *
   * @param x1  the latitude of the first point
   * @param y1  the longitude of the first point
   * @param x2  the latitude of the second point
   * @param y2  the longitude of the second point
   * @return    the distance in km
   * @see <a href="http://comopt.ifi.uni-heidelberg.de/software/TSPLIB95/tsp95.pdf">TSPLIB official documentation</a>
   */
  public static int roundedHaversineDistance(
      final double x1, final double y1, final double x2, final double y2
  ) {
    final double lat1 = ddmmToRadians(x1);
    final double lon1 = ddmmToRadians(y1);

    final double lat2 = ddmmToRadians(x2);
    final double lon2 = ddmmToRadians(y2);

    final double q1 = Math.cos(lon1 - lon2);
    final double q2 = Math.cos(lat1 - lat2);
    final double q3 = Math.cos(lat1 + lat2);
    final double d = EARTH_RADIUS * Math.acos(0.5 * ((1.0+q1) * q2 - (1.0-q1) * q3)) + 1.0;
    return (int)d;
  }

  // ==============================================================================================
  // Private helpers
  // ==============================================================================================

  /**
   * Convert the width of an angle encoded as a double in the format DDD.MMM
   * (D = degrees, M = minutes) to the corresponding value in radians.
   *
   * @param a  the angle width expressed as DDD.MMM
   * @return   the same width in radians
   */
  private static double ddmmToRadians(final double a) {
    final int deg = (int)(a);
    final double min = a - deg;
    return PI * (deg + 5.0 * min / 3.0) / 180.0;
  }

  /**
   * Returns the closest integer value.
   *
   * @param a  the value to round
   * @return   the closest integer value
   */
  private static int nint(final double a) {
    return (int)(a + 0.5);
  }
}
