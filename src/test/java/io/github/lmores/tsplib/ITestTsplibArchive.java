package io.github.lmores.tsplib;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.lmores.tsplib.tsp.TspInstance;


public class ITestTsplibArchive {
  static {
    Assertions.assertFalse(
        System.getProperty("java.class.path").contains("target/classes"),
        "Class path contains 'target/classes', but this test suite must be run from jar"
    );
  }

  @Test
  public void testTspArchiveExtractionFromJarResource() throws IOException {
    System.out.println(System.getProperty("java.class.path"));

    final TspInstance instance = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.loadTspInstance("a280"),
        "Failed to load instance from jar resources"
    );
    System.out.println(instance);
  }
}
