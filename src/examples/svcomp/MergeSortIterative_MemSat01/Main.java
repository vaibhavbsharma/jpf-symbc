package svcomp.MergeSortIterative;

import org.sosy_lab.sv_benchmarks.Verifier;

/**
 * Type : Memory Safety Expected Verdict : True Last modified by : Zafer Esen <zafer.esen@it.uu.se>
 * Date : 9 October 2019
 *
 * <p>Permission to freely use and modify the file granted via e-mail on 26.10.2019 by the original
 * author David Kosbie <koz@cmu.edu>.
 */

// IterativeMergeSort.java
// By David Kosbie

// To see iterative mergesort in action, check out the (really great!) xSortLab:
//   http://math.hws.edu/TMCM/java/xSortLab/
// Select "mergesort" and step through the algorithm visually.  Neat!

// How it works:  on the first pass, we merge a[0] and a[1], then we merge
// a[2] and a[3], and so on.  So the array of n elements contains n/2 sorted
// subarrays of size 2.  On the second pass, we merge a[0]-a[3], a[4]-a[7],
// and so on, so the array of n elements contains n/4 sorted subarrays of size
// 4.  We keep doubling our "blockSize" until it reaches n, and we're done.

public class Main {

  public static void main(String[] args) {
    final int N = Verifier.nondetInt();

    Verifier.assume(N > 0);

    int data[] = new int[N];
    for (int i = 0; i < N; i++) {
      data[i] = i;
    }

    try {
      iterativeMergesort(data);
    } catch (Exception e) {
      assert false;
    }
  }

  /////////////////////////////////////////
  // Iterative mergeSort
  /////////////////////////////////////////

  public static void iterativeMergesort(int[] a) {
    int[] aux = new int[a.length];
    for (int blockSize = 1; blockSize < a.length; blockSize *= 2)
      for (int start = 0; start < a.length; start += 2 * blockSize)
        merge(a, aux, start, start + blockSize, start + 2 * blockSize);
  }

  /////////////////////////////////////////
  // Iterative mergeSort without copy
  /////////////////////////////////////////
  

  private static void merge(int[] a, int[] aux, int lo, int mid, int hi) {
    // DK: add two tests to first verify "mid" and "hi" are in range
    if (mid >= a.length) return;
    if (hi > a.length) hi = a.length;
    int i = lo, j = mid;
    for (int k = lo; k < hi; k++) {
      if (i == mid) aux[k] = a[j++];
      else if (j == hi) aux[k] = a[i++];
      else if (a[j] < a[i]) aux[k] = a[j++];
      else aux[k] = a[i++];
    }
    // copy back
    for (int k = lo; k < hi; k++) a[k] = aux[k];
  }

}
