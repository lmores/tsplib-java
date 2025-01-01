package io.github.lmores.tsplib.atsp;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class that stores the optimal value of each ATSP instance in TSPLIB.
 *
 * Instances of this class are not allowed.
 *
 * @author  Lorenzo Moreschini
 * @since   0.0.1
 */
public class AtspOptTourValues {
  private static final Map<String, Integer> ATSP_OPT_TOUR_VALUES = buildValues();

  /** This class contains only static methods and no instance is allowed. */
  private AtspOptTourValues() { /* no-op */ }

  /**
   * Returns the optimal value for the given ATSP instance in TSPLIB.
   *
   * @param name  the name of the instance (e.g. "br17")
   * @return      the optimal value
   */
  public static int get(final String name) {
    return ATSP_OPT_TOUR_VALUES.get(name);
  }

  /**
   * Returns a map containing the optimal value for each ATSP instance in TSPLIB.
   *
   * @return  the optimal values (e.g. {@code {"br17": 39, "ft53": 6905, ...}})
   */
  public static Map<String, Integer> getAll(final String name) {
    return new HashMap<>(ATSP_OPT_TOUR_VALUES);
  }

  private static Map<String, Integer> buildValues() {
    final Map<String, Integer> values = new HashMap<>(256);
    values.put("br17", 39);
    values.put("ft53", 6905);
    values.put("ft70", 38673);
    values.put("ftv33", 1286);
    values.put("ftv35", 1473);
    values.put("ftv38", 1530);
    values.put("ftv44", 1613);
    values.put("ftv47", 1776);
    values.put("ftv55", 1608);
    values.put("ftv64", 1839);
    values.put("ftv70", 1950);
    values.put("ftv90", 1579);
    values.put("ftv100", 1788);
    values.put("ftv110", 1958);
    values.put("ftv120", 2166);
    values.put("ftv130", 2307);
    values.put("ftv140", 2420);
    values.put("ftv150", 2611);
    values.put("ftv160", 2683);
    values.put("ftv170", 2755);
    values.put("kro124", 36230);
    values.put("p43", 5620);
    values.put("rbg323", 1326);
    values.put("rbg358", 1163);
    values.put("rbg403", 2465);
    values.put("rbg443", 2720);
    values.put("ry48p", 14422);

    return values;
  }
}
