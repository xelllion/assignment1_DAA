import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        int[] data = {12, 3, 5, 7, 4, 19, 26, 0, 1, 99, 14, 2};
        int[] copy = data.clone();
        Arrays.sort(copy);

        // Let's find median index (middle element)
        int k = data.length / 2;

        long startTime = System.nanoTime();
        int result = DeterministicSelector.select(data, k);
        long endTime = System.nanoTime();

        System.out.println("Original array sorted: " + Arrays.toString(copy));
        System.out.println("Target index k: " + k + " (Expected value: " + copy[k] + ")");
        System.out.println("Found by Select: " + result);
        System.out.println("Execution time: " + (endTime - startTime) + " ns");
        System.out.println("Max recursion depth: " + DeterministicSelector.maxDepth);
        System.out.println("Total comparisons: " + DeterministicSelector.comparisons);
    }
}