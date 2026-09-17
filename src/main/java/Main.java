public class Main {
    public static void main(String[] args) {
        Point[] points = {
                new Point(2, 3),
                new Point(12, 30),
                new Point(40, 50),
                new Point(5, 1),
                new Point(12, 10),
                new Point(3, 4) // (2,3) and (3,4) distance is ~1.414
        };

        long startTime = System.nanoTime();
        ClosestPairSolver.Result res = ClosestPairSolver.findClosestPair(points);
        long endTime = System.nanoTime();

        System.out.println("Closest points: " + res.p1 + " and " + res.p2);
        System.out.println("Minimum distance: " + res.distance);
        System.out.println("Execution time: " + (endTime - startTime) + " ns");
        System.out.println("Max recursion depth: " + ClosestPairSolver.maxDepth);
        System.out.println("Distance checks: " + ClosestPairSolver.distanceCalculations);
    }
}