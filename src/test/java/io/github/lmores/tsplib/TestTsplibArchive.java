package io.github.lmores.tsplib;

import java.io.IOException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.lmores.tsplib.hcp.HcpInstance;
import io.github.lmores.tsplib.tsp.TspInstance;
import io.github.lmores.tsplib.tsp.TspOptTourValues;

public class TestTsplibArchive {

  @Test
  public void testAtspArchiveExtraction() throws IOException {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractAtspFilenames()
    );
    Assertions.assertEquals(19, filenames.length);

    int instanceCount = 0;
    for (final String fname: filenames) {
      Assertions.assertDoesNotThrow(
          () -> TsplibArchive.loadAtspInstance(fname),
          "Failed to load ATSP instance " + fname
      );
      ++instanceCount;
    }
    Assertions.assertEquals(19, instanceCount);
  }

  @Test
  public void testHcpArchiveExtraction() throws IOException {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractHcpFilenames()
    );
    Assertions.assertEquals(18, filenames.length);

    int instanceCount = 0;
    int tourCount = 0;
    for (final String fname: filenames) {
      if (fname.endsWith(".hcp")) {
        Assertions.assertDoesNotThrow(
            () -> TsplibArchive.loadHcpInstance(fname),
            "Failed to load HCP instance " + fname
        );
        ++instanceCount;

      } else if (fname.endsWith(".opt.tour")) {
        final Solutions sol = Assertions.assertDoesNotThrow(
            () -> TsplibArchive.loadHcpTour(fname),
            "Failed to load HCP solution for instance " + fname
        );
        ++tourCount;

        final String name = fname.replace(".opt.tour", "");
        final HcpInstance instance = Assertions.assertDoesNotThrow(
            () -> TsplibArchive.loadHcpInstance(name + ".hcp"),
            "Failed to load HCP instance " + name
        );

        // Just compute the value, no list with the expected values is available
        Assertions.assertDoesNotThrow(() -> instance.computeTourValue(sol.tours()[0]));
      }
    }
    Assertions.assertEquals(9, instanceCount);
    Assertions.assertEquals(9, tourCount);
  }

  @Test
  public void testSopArchiveExtraction() throws IOException {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractSopFilenames()
    );
    Assertions.assertEquals(41, filenames.length);

    int instanceCount = 0;
    for (final String fname: filenames) {
      Assertions.assertDoesNotThrow(
          () -> TsplibArchive.loadSopInstance(fname),
          "Failed to load SOP instance " + fname
      );
      ++instanceCount;
    }
    Assertions.assertEquals(41, instanceCount);
  }

  @Test
  public void testTspArchiveExtraction() throws IOException {
    final String[] filenames = Assertions.assertDoesNotThrow(
        () -> TsplibArchive.extractTspFilenames()
    );
    Assertions.assertEquals(144, filenames.length);

    int instanceCount = 0;
    int tourCount = 0;
    for (final String fname: filenames) {
      if (fname.endsWith(".tsp")) {
        Assertions.assertDoesNotThrow(
            () -> TsplibArchive.loadTspInstance(fname),
            "Failed to load TSP instance " + fname
        );
        ++instanceCount;

      } else if (fname.endsWith(".opt.tour")) {
        final Solutions tour = Assertions.assertDoesNotThrow(
            () -> TsplibArchive.loadTspTour(fname),
            "Failed to load TSP tours for instance " + fname
        );
        ++tourCount;

        final String name = fname.replace(".opt.tour", "");
        final TspInstance instance = Assertions.assertDoesNotThrow(
            () -> TsplibArchive.loadTspInstance(name + ".tsp"),
            "Failed to load TSP instance " + name
        );

        final int expectedValue = TspOptTourValues.get(name);
        final int actualValue = instance.computeTourValue(tour.tours()[0]);
        Assertions.assertEquals(expectedValue, actualValue);
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

    int instanceCount = 0;
    for (final String fname: filenames) {
      Assertions.assertDoesNotThrow(
          () -> TsplibArchive.loadVrpInstance(fname),
          "Failed to load VRP instance " + fname
      );
      ++instanceCount;
    }
    Assertions.assertEquals(16, instanceCount);
  }
}
