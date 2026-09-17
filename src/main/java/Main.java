import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] data = {55, 12, 89, 4, 23, 77, 90, 1, 15, 33};

        System.out.println("Before QuickSort: " + Arrays.toString(data));

        long startTime = System.nanoTime();
        QuickSorter.sort(data);
        long endTime = System.nanoTime();

        System.out.println("After QuickSort: " + Arrays.toString(data));
        System.out.println("Execution time: " + (endTime - startTime) + " ns");
        System.out.println("Max recursion depth: " + QuickSorter.maxDepth);
        System.out.println("Total comparisons: " + QuickSorter.comparisons);
        System.out.println("Total swaps: " + QuickSorter.swaps);
    }
}