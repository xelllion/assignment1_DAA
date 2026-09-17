import java.util.Arrays;

public class DeterministicSelector {

    // Metrics required by assignment
    public static int maxDepth = 0;
    public static long comparisons = 0;

    /**
     * Finds the k-th smallest element (0-indexed).
     * e.g., k = 0 returns the minimum, k = a.length / 2 returns the median.
     */
    public static int select(int[] a, int k) {
        if (a == null || a.length == 0 || k < 0 || k >= a.length) {
            throw new IllegalArgumentException("Invalid input array or index k");
        }

        // Reset metrics before running
        maxDepth = 0;
        comparisons = 0;

        return select(a, 0, a.length - 1, k, 1);
    }

    private static int select(int[] a, int low, int high, int k, int currentDepth) {
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }

        // If subarray has only one element
        if (low == high) {
            return a[low];
        }

        // Step 1 & 2: Get median-of-medians as pivot
        int pivot = getMedianOfMedians(a, low, high, currentDepth);

        // Step 3: In-place partition around the pivot value
        int pivotIndex = partition(a, low, high, pivot);

        // Step 4: Recurse ONLY into the required partition
        if (k == pivotIndex) {
            return a[k];
        } else if (k < pivotIndex) {
            return select(a, low, pivotIndex - 1, k, currentDepth + 1);
        } else {
            return select(a, pivotIndex + 1, high, k, currentDepth + 1);
        }
    }

    private static int getMedianOfMedians(int[] a, int low, int high, int currentDepth) {
        int n = high - low + 1;
        if (n <= 5) {
            return findMedianOfSmallGroup(a, low, high);
        }

        // Divide array into groups of 5 and collect their medians
        int numGroups = (n + 4) / 5;
        for (int i = 0; i < numGroups; i++) {
            int groupLow = low + i * 5;
            int groupHigh = Math.min(groupLow + 4, high);
            int medianVal = findMedianOfSmallGroup(a, groupLow, groupHigh);

            // Move medians to the beginning of the subarray: low, low+1, ...
            swap(a, low + i, indexOf(a, groupLow, groupHigh, medianVal));
        }

        // Recursively find the median of the medians
        int mediansStart = low;
        int mediansEnd = low + numGroups - 1;
        int medianOfMediansIndex = low + (numGroups - 1) / 2;

        return select(a, mediansStart, mediansEnd, medianOfMediansIndex, currentDepth + 1);
    }

    private static int findMedianOfSmallGroup(int[] a, int low, int high) {
        // Sort small chunk (<= 5 elements) using simple insertion sort
        for (int i = low + 1; i <= high; i++) {
            int key = a[i];
            int j = i - 1;
            while (j >= low && a[j] > key) {
                comparisons++;
                a[j + 1] = a[j];
                j--;
            }
            if (j >= low) comparisons++;
            a[j + 1] = key;
        }
        return a[low + (high - low) / 2];
    }

    private static int partition(int[] a, int low, int high, int pivot) {
        // Locate pivot element and move it to the end
        for (int i = low; i <= high; i++) {
            if (a[i] == pivot) {
                swap(a, i, high);
                break;
            }
        }

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

    private static int indexOf(int[] a, int low, int high, int target) {
        for (int i = low; i <= high; i++) {
            if (a[i] == target) return i;
        }
        return low;
    }

    private static void swap(int[] a, int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }
}