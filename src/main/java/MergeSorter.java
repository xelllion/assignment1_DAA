public class MergeSorter {

    // Small-input cutoff threshold
    private static final int CUTOFF = 10;

    // Metrics required by assignment
    public static int maxDepth = 0;
    public static long comparisons = 0;

    public static void sort(int[] a) {
        if (a == null || a.length <= 1) return;

        // Reset metrics before running
        maxDepth = 0;
        comparisons = 0;

        // Reusable auxiliary buffer
        int[] buffer = new int[a.length];

        mergeSort(a, buffer, 0, a.length - 1, 1);
    }

    private static void mergeSort(int[] a, int[] buffer, int left, int right, int currentDepth) {
        // Track maximum recursion depth
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }

        // Small-input cutoff: switch to insertion sort for small subarrays
        if (right - left + 1 <= CUTOFF) {
            insertionSort(a, left, right);
            return;
        }

        int mid = left + (right - left) / 2;

        // Recursively sort left and right halves
        mergeSort(a, buffer, left, mid, currentDepth + 1);
        mergeSort(a, buffer, mid + 1, right, currentDepth + 1);

        // Merge the sorted halves
        merge(a, buffer, left, mid, right);
    }

    private static void merge(int[] a, int[] buffer, int left, int mid, int right) {
        // Copy segment to auxiliary buffer
        for (int i = left; i <= right; i++) {
            buffer[i] = a[i];
        }

        int i = left;      // Pointer for left half
        int j = mid + 1;   // Pointer for right half
        int k = left;      // Pointer for main array

        while (i <= mid && j <= right) {
            comparisons++;
            if (buffer[i] <= buffer[j]) {
                a[k++] = buffer[i++];
            } else {
                a[k++] = buffer[j++];
            }
        }

        // Copy remaining elements from left half
        while (i <= mid) {
            a[k++] = buffer[i++];
        }
    }

    // Helper insertion sort for small arrays
    private static void insertionSort(int[] a, int left, int right) {
        for (int i = left + 1; i <= right; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= left && a[j] > key) {
                comparisons++;
                a[j + 1] = a[j];
                j--;
            }
            if (j >= left) comparisons++;
            a[j + 1] = key;
        }
    }
}