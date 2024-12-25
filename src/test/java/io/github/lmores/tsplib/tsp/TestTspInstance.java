package io.github.lmores.tsplib.tsp;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.lmores.tsplib.TsplibArchive;


public class TestTspInstance {

  @Test
  public void readTspInstance() throws URISyntaxException, IOException {
    final TspInstance pcb442 = TsplibArchive.loadTspInstance("pcb442.tsp");
    Assertions.assertEquals(221440, computeCanonicalTourLength(pcb442));

    final TspInstance gr666 = TsplibArchive.loadTspInstance("gr666.tsp");
    Assertions.assertEquals(423710, computeCanonicalTourLength(gr666));

    final TspInstance att532 = TsplibArchive.loadTspInstance("att532.tsp");
    Assertions.assertEquals(309636, computeCanonicalTourLength(att532));
  }

  // ==========================================================================
  // Private helpers
  // ==========================================================================

  private double computeCanonicalTourLength(final TspInstance instance) {
    final int lastNodeIdx = instance.getDimension() - 1;

    double length = instance.getEdgeWeight(0, lastNodeIdx);
    for (int i = 0; i < lastNodeIdx; ++i) {
      length += instance.getEdgeWeight(i, i + 1);
    }

    return length;
  }
}
