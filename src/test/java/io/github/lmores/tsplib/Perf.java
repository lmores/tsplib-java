package io.github.lmores.tsplib;

import java.util.function.BiFunction;

public class Perf {
  public static void main(String[] args) {
    lambdaPerf(10_000);
  }

  private static void lambdaPerf(final int repetitions) {
    double start = System.currentTimeMillis();
    for (int i = 0; i < repetitions; ++i) {
      runLambdaDistanceFunc();
    }
    double end = System.currentTimeMillis();
    System.out.println("Lambda func took " + (end - start) + " ms (iterations: " + repetitions + ")");

    start = System.currentTimeMillis();
    for (int i = 0; i < repetitions; ++i) {
      runNativeDistanceFunc();
    }
    end = System.currentTimeMillis();
    System.out.println("Native func took " + (end - start) + " ms (iterations: " + repetitions + ")");
  }

  private static void runLambdaDistanceFunc() {
    final BiFunction<double[], double[], Integer> func = (p, q) -> TsplibUtil.roundedChebyshevDistance(p[0], p[1], q[0], q[1]);
    for (int i = 0; i < 1_000_000; ++i) {
      for (int j = 0; j < 1_000_000; ++j) {
        func.apply(new double[] {i, i}, new double[] {j, j});
      }
    }
  }

  private static void runNativeDistanceFunc() {
    for (int i = 0; i < 1_000_000; ++i) {
      for (int j = 0; j < 1_000_000; ++j) {
        final double[] p = new double[] {i, i};
        final double[] q = new double[] {j, j};
        TsplibUtil.roundedChebyshevDistance(p[0], p[1], q[0], q[1]);
      }
    }
  }
}
