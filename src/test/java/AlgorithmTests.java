import java.util.Arrays;
import java.util.Random;

public class AlgorithmTests {

    private static final Random random = new Random(12345);

    public static void main(String[] args) {
        System.out.println("Running automated correctness tests...\n");

        testSortingAlgorithms();
        testDeterministicSelect();
        testClosestPair();

        System.out.println("\nALL TESTS PASSED SUCCESSFULLY!");
    }

    private static void testSortingAlgorithms() {
        System.out.println("--- Testing MergeSort and QuickSort ---");

        // Edge cases required by specification: empty array and single-element array
        testSortSingleCase("Empty array", new int[]{});
        testSortSingleCase("Single element array", new int[]{42});

        // Input variations required: Random, Sorted, Reverse-sorted, Duplicate-heavy
        int n = 1000;
        testSortSingleCase("Random array", generateArray(n, "Random"));
        testSortSingleCase("Already sorted array", generateArray(n, "Sorted"));
        testSortSingleCase("Reverse sorted array", generateArray(n, "Reverse-sorted"));
        testSortSingleCase("Duplicate-heavy array", generateArray(n, "Duplicate-heavy"));

        System.out.println("Sorting tests passed.\n");
    }

    private static void testSortSingleCase(String label, int[] original) {
        int[] expected = original.clone();
        Arrays.sort(expected);

        // Test MergeSort
        int[] mergeTarget = original.clone();
        MergeSorter.sort(mergeTarget);
        if (!Arrays.equals(mergeTarget, expected)) {
            throw new AssertionError("MergeSort failed for: " + label);
        }

        // Test QuickSort
        int[] quickTarget = original.clone();
        QuickSorter.sort(quickTarget);
        if (!Arrays.equals(quickTarget, expected)) {
            throw new AssertionError("QuickSort failed for: " + label);
        }

        System.out.println("[PASS] " + label);
    }

    private static void testDeterministicSelect() {
        System.out.println("--- Testing Deterministic Select (100 random runs) ---");

        // Required: at least 100 random tests comparing with Arrays.sort(a)[k]
        for (int i = 0; i < 100; i++) {
            int n = 10 + random.nextInt(500);
            int[] arr = generateArray(n, "Random");
            int k = random.nextInt(n);

            int[] sortedCopy = arr.clone();
            Arrays.sort(sortedCopy);
            int expectedValue = sortedCopy[k];

            int actualValue = DeterministicSelector.select(arr, k);

            if (actualValue != expectedValue) {
                throw new AssertionError(String.format(
                        "DeterministicSelect failed at run %d! Expected %d, but got %d",
                        i + 1, expectedValue, actualValue
                ));
            }
        }
        System.out.println("[PASS] 100/100 random test iterations verified against Arrays.sort()[k].\n");
    }

    private static void testClosestPair() {
        System.out.println("--- Testing Closest Pair of Points (n <= 2000 against Brute Force) ---");

        int[] testSizes = {10, 50, 200, 1000, 2000};

        for (int n : testSizes) {
            Point[] points = generatePoints(n);

            ClosestPairSolver.Result fastResult = ClosestPairSolver.findClosestPair(points);
            ClosestPairSolver.Result bruteResult = ClosestPairSolver.bruteForce(points);

            // Floating point tolerance check
            if (Math.abs(fastResult.distance - bruteResult.distance) > 1e-9) {
                throw new AssertionError(String.format(
                        "ClosestPair failed for n=%d! Divide&Conquer distance: %f, Brute Force: %f",
                        n, fastResult.distance, bruteResult.distance
                ));
            }
            System.out.println(String.format("[PASS] Dataset n=%d matches brute force (Distance: %.4f)", n, fastResult.distance));
        }
        System.out.println("Closest Pair tests passed.");
    }

    private static int[] generateArray(int n, String type) {
        int[] a = new int[n];
        switch (type) {
            case "Random":
                for (int i = 0; i < n; i++) a[i] = random.nextInt(100_000);
                break;
            case "Sorted":
                for (int i = 0; i < n; i++) a[i] = i;
                break;
            case "Reverse-sorted":
                for (int i = 0; i < n; i++) a[i] = n - i;
                break;
            case "Duplicate-heavy":
                for (int i = 0; i < n; i++) a[i] = random.nextInt(5);
                break;
        }
        return a;
    }

    private static Point[] generatePoints(int n) {
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point(random.nextDouble() * 5000, random.nextDouble() * 5000);
        }
        return pts;
    }
}