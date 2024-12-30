package io.github.lmores.tsplib;

import java.io.IOException;
import java.util.Arrays;

import io.github.lmores.tsplib.tsp.TspInstance;

public class Main {
  public static void main(final String[] args) throws IOException {
    usageExample();
  }

  private static void usageExample() throws IOException {
    String[] filenames = TsplibArchive.extractTspFilenames();
    System.out.println(Arrays.toString(filenames));

    TspInstance instance = TsplibArchive.loadTspInstance("a280.tsp");
    System.out.println(instance.name());            // "a280"
    // System.out.println(instance.comment());         // "drilling problem (Ludwig)"
    System.out.println(instance.dimension());       // 280
    // System.out.println(instance.getEdgeWeightType());  // EUC_2D
    System.out.println(instance.getEdgeWeight(0, 1));  // 20
  }
}
