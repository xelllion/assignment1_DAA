import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] data = {38, 27, 43, 3, 9, 82, 10, 19, 50, 61, 2, 4};

        System.out.println("Before sorting: " + Arrays.toString(data));

        long startTime = System.nanoTime();
        MergeSorter.sort(data);
        long endTime = System.nanoTime();

        System.out.println("After sorting: " + Arrays.toString(data));
        System.out.println("Execution time: " + (endTime - startTime) + " ns");
        System.out.println("Max recursion depth: " + MergeSorter.maxDepth);
        System.out.println("Comparisons count: " + MergeSorter.comparisons);
    }
}