package io.github.lmores.tsplib;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTsplibArchive {
  @Test
  public void testAtspArchiveExtraction() throws IOException {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractAtspFilenames()
    );
    Assertions.assertEquals(19, filenames.length);

    for (final String fname: filenames) {
      TsplibFileData.read(TestTsplibArchive.class.getResourceAsStream("atsp/" + fname));
    }
  }

  @Test
  public void testHcpArchiveExtraction() throws IOException {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractHcpFilenames()
    );
    Assertions.assertEquals(18, filenames.length);

    for (final String fname: filenames) {
      TsplibFileData.read(TestTsplibArchive.class.getResourceAsStream("hcp/" + fname));
    }
  }

  @Test
  public void testSopArchiveExtraction() throws IOException {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractSopFilenames()
    );
    Assertions.assertEquals(41, filenames.length);

    for (final String fname: filenames) {
      TsplibFileData.read(TestTsplibArchive.class.getResourceAsStream("sop/" + fname));
    }
  }

  @Test
  public void testTspArchiveExtraction() {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractTspFilenames()
    );
    Assertions.assertEquals(144, filenames.length);

    int instanceCount = 0;
    int tourCount = 0;
    for (final String fname: filenames) {
      if (fname.endsWith(".tsp")) {
        Assertions.assertDoesNotThrow(() -> TsplibArchive.loadTspInstance(fname));
        ++instanceCount;
      }

      if (fname.endsWith(".tour")) {
        Assertions.assertDoesNotThrow(() -> TsplibArchive.loadTspTour(fname));
        ++tourCount;
      }
    }
    Assertions.assertEquals(111, instanceCount);
    Assertions.assertEquals(32, tourCount);
  }

  @Test
  public void testVrpArchiveExtraction() throws IOException {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractVrpFilenames()
    );
    Assertions.assertEquals(16, filenames.length);

    for (final String fname: filenames) {
      TsplibFileData.read(TestTsplibArchive.class.getResourceAsStream("vrp/" + fname));
    }
  }
}
