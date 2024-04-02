package io.github.lmores.tsplib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class ITTsplibArchive {
  
  static {
    Assertions.assertFalse(System.getProperty("java.class.path").contains("target/classes"));
  }

  @Test
  public void testArchiveExtractionFromJarResource() {
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
