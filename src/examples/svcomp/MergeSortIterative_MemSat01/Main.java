package svcomp.MergeSortIterative_MemSat01;

import org.sosy_lab.sv_benchmarks.Verifier;


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

  public static void iterativeMergesort(int[] a) {
    int[] aux = new int[a.length];
    for (int blockSize = 1; blockSize < a.length; blockSize *= 2)
      for (int start = 0; start < a.length; start += 2 * blockSize)
        merge(a, aux, start, start + blockSize, start + 2 * blockSize);
  }

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
