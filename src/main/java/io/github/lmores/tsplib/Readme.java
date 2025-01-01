package io.github.lmores.tsplib;

import java.io.IOException;
import java.util.Arrays;

import io.github.lmores.tsplib.tsp.TspInstance;

/**
 * The sole purpose of this class is to run the snippets used in README.md.
 *
 * This class is not included in the jar archive.
 *
 * @author  Lorenzo Moreschini
 * @since   0.0.1
 */
public class Readme {
  public static void main(final String[] args) throws IOException {
    usageExample();
  }

  private static void usageExample() throws IOException {
    String[] filenames = TsplibArchive.extractTspFilenames();
    System.out.println(Arrays.toString(filenames));    // ["a280.opt.tour", "a280.tsp", "ali535.tsp", ...]

    TspInstance instance = TsplibArchive.loadTspInstance("a280.tsp");
    System.out.println(instance.name());               // "a280"
    System.out.println(instance.comment());            // "drilling problem (Ludwig)"
    System.out.println(instance.dimension());          // 280
    System.out.println(instance.edgeWeightType());     // EUC_2D
    System.out.println(instance.getEdgeWeight(0, 1));  // 20

    Solutions solutions = TsplibArchive.loadTspTour("a280.opt.tour");
    System.out.println(solutions.name());                       // "./TSPLIB/a280.tsp.optbc.tour"
    System.out.println(solutions.comment());                    // ""
    System.out.println(solutions.dimension());                  // 280
    System.out.println(solutions.tours().length);               // 1
    System.out.println(Arrays.toString(solutions.tours()[0]));  // [0, 1, 241, 242, 243, ...]

    // Analogous methods are available also for ATSP, HCP, SOP and VRP instances (see the javadoc).
  }
}
