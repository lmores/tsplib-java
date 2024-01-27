package io.github.lmores.tsplib;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTsplibArchive {

  @Test
  public void testArchiveExtraction() throws URISyntaxException, IOException {
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
}
