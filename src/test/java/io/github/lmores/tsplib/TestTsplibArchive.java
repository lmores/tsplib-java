package io.github.lmores.tsplib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTsplibArchive {
  @Test
  public void testTspArchiveExtraction() {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractTspFilenames()
    );
    Assertions.assertEquals(144, filenames.length);

    for (final String fname: filenames) {
      if (fname.endsWith(".tsp")) {
        Assertions.assertDoesNotThrow(() -> TsplibArchive.readTspInstance(fname));
      }
    }
  }

  @Test
  public void testTspTourArchiveExtraction() {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractTspFilenames()
    );
    Assertions.assertEquals(144, filenames.length);

    int tourCount = 0;
    for (final String fname: filenames) {
      if (fname.endsWith(".tour")) {
        Assertions.assertDoesNotThrow(() -> TsplibArchive.readTspTour(fname));
        ++tourCount;
      }
    }

    Assertions.assertEquals(32, tourCount);
  }
}
