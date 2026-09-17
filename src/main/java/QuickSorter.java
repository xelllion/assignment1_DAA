import java.util.Random;

public class QuickSorter {

    private static final Random random = new Random();

    // Metrics required by assignment
    public static int maxDepth = 0;
    public static long comparisons = 0;
    public static long swaps = 0;

    public static void sort(int[] a) {
        if (a == null || a.length <= 1) return;

        // Reset metrics before running
        maxDepth = 0;
        comparisons = 0;
        swaps = 0;

        quickSort(a, 0, a.length - 1, 1);
    }

    private static void quickSort(int[] a, int low, int high, int currentDepth) {
        while (low < high) {
            if (currentDepth > maxDepth) {
                maxDepth = currentDepth;
            }

            // In-place partition with randomized pivot
            int pIndex = randomizedPartition(a, low, high);

            // Optimization: recurse on the smaller partition, loop on the larger one
            int leftSize = pIndex - 1 - low;
            int rightSize = high - (pIndex + 1);

            if (leftSize < rightSize) {
                quickSort(a, low, pIndex - 1, currentDepth + 1);
                low = pIndex + 1; // loop on the larger right side
            } else {
                quickSort(a, pIndex + 1, high, currentDepth + 1);
                high = pIndex - 1; // loop on the larger left side
            }
        }
    }

    private static int randomizedPartition(int[] a, int low, int high) {
        // Pick random pivot index between low and high
        int pivotIndex = low + random.nextInt(high - low + 1);
        swap(a, pivotIndex, high);

        int pivot = a[high];
        int i = low - 1;

        for (int j = low; j < high; j++) {
            comparisons++;
            if (a[j] <= pivot) {
                i++;
                swap(a, i, j);
            }
        }
        swap(a, i + 1, high);
        return i + 1;
    }

    private static void swap(int[] a, int i, int j) {
        if (i == j) return;
        swaps++;
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}