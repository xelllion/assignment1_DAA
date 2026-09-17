import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class ClosestPairSolver {

    // Metrics required by assignment
    public static int maxDepth = 0;
    public static long distanceCalculations = 0;

    public static class Result {
        public final Point p1;
        public final Point p2;
        public final double distance;

        public Result(Point p1, Point p2, double distance) {
            this.p1 = p1;
            this.p2 = p2;
            this.distance = distance;
        }
    }

    public static Result findClosestPair(Point[] points) {
        if (points == null || points.length < 2) {
            throw new IllegalArgumentException("At least 2 points are required.");
        }

        // Reset metrics
        maxDepth = 0;
        distanceCalculations = 0;

        // Sort by X-coordinate
        Point[] ptsSortedByX = points.clone();
        Arrays.sort(ptsSortedByX, Comparator.comparingDouble(p -> p.x));

        return closestPairRecursive(ptsSortedByX, 0, ptsSortedByX.length - 1, 1);
    }

    private static Result closestPairRecursive(Point[] ptsByX, int low, int high, int currentDepth) {
        if (currentDepth > maxDepth) {
            maxDepth = currentDepth;
        }

        int count = high - low + 1;

        // Small input cutoff: use brute force for 3 or fewer points
        if (count <= 3) {
            return bruteForceRange(ptsByX, low, high);
        }

        int mid = low + (high - low) / 2;
        Point midPoint = ptsByX[mid];

        // Recurse left and right
        Result leftRes = closestPairRecursive(ptsByX, low, mid, currentDepth + 1);
        Result rightRes = closestPairRecursive(ptsByX, mid + 1, high, currentDepth + 1);

        Result minRes = (leftRes.distance < rightRes.distance) ? leftRes : rightRes;
        double d = minRes.distance;

        // Build strip around midPoint within distance d
        List<Point> strip = new ArrayList<>();
        for (int i = low; i <= high; i++) {
            if (Math.abs(ptsByX[i].x - midPoint.x) < d) {
                strip.add(ptsByX[i]);
            }
        }

        // Sort strip by Y-coordinate
        strip.sort(Comparator.comparingDouble(p -> p.y));

        // Check pairs in strip (at most 7-8 points ahead)
        Result stripMin = minRes;
        for (int i = 0; i < strip.size(); i++) {
            Point p1 = strip.get(i);
            for (int j = i + 1; j < strip.size() && (strip.get(j).y - p1.y) < d; j++) {
                Point p2 = strip.get(j);
                distanceCalculations++;
                double dist = p1.distanceTo(p2);
                if (dist < d) {
                    d = dist;
                    stripMin = new Result(p1, p2, dist);
                }
            }
        }

        return stripMin;
    }

    // Brute force method for comparison and verification
    public static Result bruteForce(Point[] points) {
        return bruteForceRange(points, 0, points.length - 1);
    }

    private static Result bruteForceRange(Point[] points, int low, int high) {
        double minDist = Double.POSITIVE_INFINITY;
        Point p1 = null;
        Point p2 = null;

        for (int i = low; i <= high; i++) {
            for (int j = i + 1; j <= high; j++) {
                distanceCalculations++;
                double dist = points[i].distanceTo(points[j]);
                if (dist < minDist) {
                    minDist = dist;
                    p1 = points[i];
                    p2 = points[j];
                }
            }
        }
        return new Result(p1, p2, minDist);
    }
}