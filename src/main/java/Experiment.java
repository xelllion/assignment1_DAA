import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Experiment {

    // Fixed seed for reproducible benchmarks
    private static final Random random = new Random(42);

    public static void main(String[] args) {
        // Create results directory if it does not exist
        File dir = new File("results");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File csvFile = new File("results/results.csv");

        try (PrintWriter writer = new PrintWriter(new FileWriter(csvFile))) {
            // CSV header line
            writer.println("Algorithm,InputType,Size,ExecutionTimeNs,MaxDepth,ExtraMetricName,ExtraMetricValue");

            int[] sizes = {100, 1000, 5000, 10000, 25000};
            String[] dataTypes = {"Random", "Sorted", "Reverse-sorted", "Duplicate-heavy"};

            System.out.println("Starting experiments, please wait...");

            // 1. Benchmarking MergeSort and QuickSort
            for (int n : sizes) {
                for (String type : dataTypes) {
                    // MergeSort benchmark
                    int[] data1 = generateArray(n, type);
                    long start = System.nanoTime();
                    MergeSorter.sort(data1);
                    long duration = System.nanoTime() - start;
                    writer.printf("MergeSort,%s,%d,%d,%d,Comparisons,%d%n",
                            type, n, duration, MergeSorter.maxDepth, MergeSorter.comparisons);

                    // QuickSort benchmark
                    int[] data2 = generateArray(n, type);
                    start = System.nanoTime();
                    QuickSorter.sort(data2);
                    duration = System.nanoTime() - start;
                    writer.printf("QuickSort,%s,%d,%d,%d,Comparisons,%d%n",
                            type, n, duration, QuickSorter.maxDepth, QuickSorter.comparisons);
                }
            }

            // 2. Benchmarking Deterministic Select (finding median: k = n / 2)
            for (int n : sizes) {
                for (String type : dataTypes) {
                    int[] data = generateArray(n, type);
                    int k = n / 2;
                    long start = System.nanoTime();
                    DeterministicSelector.select(data, k);
                    long duration = System.nanoTime() - start;
                    writer.printf("DeterministicSelect,%s,%d,%d,%d,Comparisons,%d%n",
                            type, n, duration, DeterministicSelector.maxDepth, DeterministicSelector.comparisons);
                }
            }

            // 3. Benchmarking Closest Pair of Points
            int[] pointSizes = {100, 500, 1000, 2000, 5000, 10000};
            for (int n : pointSizes) {
                Point[] pts = generatePoints(n);
                long start = System.nanoTime();
                ClosestPairSolver.findClosestPair(pts);
                long duration = System.nanoTime() - start;
                writer.printf("ClosestPair,Random,%d,%d,%d,DistanceChecks,%d%n",
                        n, duration, ClosestPairSolver.maxDepth, ClosestPairSolver.distanceCalculations);
            }

            System.out.println("Experiments completed successfully! Results saved to results/results.csv");

        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }
    }

    // Dataset generators required by specification
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
                for (int i = 0; i < n; i++) a[i] = random.nextInt(5); // Only 5 unique values
                break;
        }
        return a;
    }

    private static Point[] generatePoints(int n) {
        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            pts[i] = new Point(random.nextDouble() * 10000, random.nextDouble() * 10000);
        }
        return pts;
    }
}