package io.github.lmores.tsplib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ITestTsplibArchive {
  static {
    Assertions.assertFalse(
        System.getProperty("java.class.path").contains("target/classes"),
        "Class path contains 'target/classes', but this test suite must be run from jar"
    );
  }

  @Test
  public void testTspArchiveExtractionFromJarResource() {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractTspFilenames()
    );
    Assertions.assertEquals(145, filenames.length);

    int instanceCount = 0;
    int tourCount = 0;
    for (final String fname: filenames) {
      if (fname.endsWith(".tsp")) {
        Assertions.assertDoesNotThrow(() -> TsplibArchive.loadTspInstance(fname));
        ++instanceCount;
      } else if (fname.endsWith(".tour")) {
        Assertions.assertDoesNotThrow(() -> TsplibArchive.loadTspTour(fname));
        ++tourCount;
      }
    }
    Assertions.assertEquals(111, instanceCount);
    Assertions.assertEquals(33, tourCount);
  }
}
