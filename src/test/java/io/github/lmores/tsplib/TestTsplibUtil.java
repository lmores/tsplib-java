package io.github.lmores.tsplib;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestTsplibUtil {

  @Test
  public void testIndexesConversion() {
    final int r = 100;

    for (int n = 0; n < r; ++n) {
      for (int i = 0; i < n; ++i) {
        for (int j = i + 1; j < n; ++j) {
          final int k = TsplibUtil.strictUpperTriangularToArrayIndex(i, j, n);
          final int[] idxs = TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(k, n);
          Assertions.assertArrayEquals(new int[] {i,j}, idxs);
        }
      }
    }

    for (int n = 0; n < r; ++n) {
      for (int k = 0, N = n * n; k < N; ++k) {
        final int[] idxs = TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(k, n);
        final int l = TsplibUtil.strictUpperTriangularToArrayIndex(idxs[0], idxs[1], n);
        Assertions.assertEquals(k, l);
      }
    }
  }

  @Test
  public void testToMatrixIndexes() {
    Assertions.assertArrayEquals(new int[] {0, 1}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(0, 2));

    Assertions.assertArrayEquals(new int[] {0, 1}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(0, 3));
    Assertions.assertArrayEquals(new int[] {0, 2}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(1, 3));
    Assertions.assertArrayEquals(new int[] {1, 2}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(2, 3));

    Assertions.assertArrayEquals(new int[] {0, 1}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(0, 4));
    Assertions.assertArrayEquals(new int[] {0, 2}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(1, 4));
    Assertions.assertArrayEquals(new int[] {0, 3}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(2, 4));
    Assertions.assertArrayEquals(new int[] {1, 2}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(3, 4));
    Assertions.assertArrayEquals(new int[] {1, 3}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(4, 4));
    Assertions.assertArrayEquals(new int[] {2, 3}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(5, 4));

    Assertions.assertArrayEquals(new int[] {0, 1}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(0, 5));
    Assertions.assertArrayEquals(new int[] {0, 2}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(1, 5));
    Assertions.assertArrayEquals(new int[] {0, 3}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(2, 5));
    Assertions.assertArrayEquals(new int[] {0, 4}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(3, 5));
    Assertions.assertArrayEquals(new int[] {1, 2}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(4, 5));
    Assertions.assertArrayEquals(new int[] {1, 3}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(5, 5));
    Assertions.assertArrayEquals(new int[] {1, 4}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(6, 5));
    Assertions.assertArrayEquals(new int[] {2, 3}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(7, 5));
    Assertions.assertArrayEquals(new int[] {2, 4}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(8, 5));
    Assertions.assertArrayEquals(new int[] {3, 4}, TsplibUtil.arrayToStrictUpperTriangularMatrixIndexes(9, 5));
  }

  @Test
  public void testToStrictUpperTriangularArrayIndexes() {
    Assertions.assertEquals(0, TsplibUtil.strictUpperTriangularToArrayIndex(0, 1, 2));

    Assertions.assertEquals(0, TsplibUtil.strictUpperTriangularToArrayIndex(0, 1, 3));
    Assertions.assertEquals(1, TsplibUtil.strictUpperTriangularToArrayIndex(0, 2, 3));
    Assertions.assertEquals(2, TsplibUtil.strictUpperTriangularToArrayIndex(1, 2, 3));

    Assertions.assertEquals(0, TsplibUtil.strictUpperTriangularToArrayIndex(0, 1, 4));
    Assertions.assertEquals(1, TsplibUtil.strictUpperTriangularToArrayIndex(0, 2, 4));
    Assertions.assertEquals(2, TsplibUtil.strictUpperTriangularToArrayIndex(0, 3, 4));
    Assertions.assertEquals(3, TsplibUtil.strictUpperTriangularToArrayIndex(1, 2, 4));
    Assertions.assertEquals(4, TsplibUtil.strictUpperTriangularToArrayIndex(1, 3, 4));
    Assertions.assertEquals(5, TsplibUtil.strictUpperTriangularToArrayIndex(2, 3, 4));

    Assertions.assertEquals(0, TsplibUtil.strictUpperTriangularToArrayIndex(0, 1, 5));
    Assertions.assertEquals(1, TsplibUtil.strictUpperTriangularToArrayIndex(0, 2, 5));
    Assertions.assertEquals(2, TsplibUtil.strictUpperTriangularToArrayIndex(0, 3, 5));
    Assertions.assertEquals(3, TsplibUtil.strictUpperTriangularToArrayIndex(0, 4, 5));
    Assertions.assertEquals(4, TsplibUtil.strictUpperTriangularToArrayIndex(1, 2, 5));
    Assertions.assertEquals(5, TsplibUtil.strictUpperTriangularToArrayIndex(1, 3, 5));
    Assertions.assertEquals(6, TsplibUtil.strictUpperTriangularToArrayIndex(1, 4, 5));
    Assertions.assertEquals(7, TsplibUtil.strictUpperTriangularToArrayIndex(2, 3, 5));
    Assertions.assertEquals(8, TsplibUtil.strictUpperTriangularToArrayIndex(2, 4, 5));
    Assertions.assertEquals(9, TsplibUtil.strictUpperTriangularToArrayIndex(3, 4, 5));
  }
}
