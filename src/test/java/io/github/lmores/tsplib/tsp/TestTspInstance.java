package io.github.lmores.tsplib.tsp;

import java.io.IOException;
import java.net.URISyntaxException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.lmores.tsplib.TsplibArchive;


public class TestTspInstance {

  @Test
  public void testCanonicalTours() throws URISyntaxException, IOException {
    final TspInstance pcb442 = TsplibArchive.loadTspInstance("pcb442.tsp");
    Assertions.assertEquals(221440, computeCanonicalTourValue(pcb442));

    final TspInstance gr666 = TsplibArchive.loadTspInstance("gr666.tsp");
    Assertions.assertEquals(423710, computeCanonicalTourValue(gr666));

    final TspInstance att532 = TsplibArchive.loadTspInstance("att532.tsp");
    Assertions.assertEquals(309636, computeCanonicalTourValue(att532));
  }

  // ==========================================================================
  // Private helpers
  // ==========================================================================

  private double computeCanonicalTourValue(final TspInstance instance) {
    final int nNodes = instance.getDimension();
    final int[] canonicalTour = new int[nNodes];
    for (int i = 0; i < nNodes; ++i)  canonicalTour[i] = i;

    return instance.computeTourValue(canonicalTour);
  }
}
