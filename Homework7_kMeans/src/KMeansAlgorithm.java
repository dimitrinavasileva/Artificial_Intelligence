import dataset.Dataset;
import dataset.Point2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class KMeansAlgorithm {
    private static final int MAX_ITERATIONS = Short.MAX_VALUE;

    private static double temp_min_distance;
    public static double sum_distance;

    public static List<Point2D> KMeans(Dataset dataset, int k) {

        List<Point2D> centroids = getRandomCentroids(dataset, k);

        int iterations = 0;
        List<Point2D> oldCentroids = null;

        while (!shouldStop(oldCentroids, centroids, iterations)) {
            oldCentroids = centroids;
            iterations++;

            // Assign labels to each datapoint based on centroids
            List<Point2D> labels = getLabels(dataset, centroids);

            // Assign centroids based on datapoint labels
            centroids = getCentroids(dataset, labels, centroids);
        }

        sum_distance = 0;

        for(Point2D p : dataset.getEntries())
        {
            getClosestCentroid(p, centroids);
            sum_distance += temp_min_distance;
        }

        return centroids;
    }

    private static List<Point2D> getCentroids(Dataset dataset, List<Point2D> labels, List<Point2D> oldCentroids) {
        List<Point2D> centroids = new ArrayList<>();

        List<Point2D> entries = dataset.getEntries();

        for (Point2D current : oldCentroids) {
            double sumX = 0;
            double sumY = 0;
            double count = 0;

            for (int j = 0; j < labels.size(); j++) {
                if (labels.get(j).equals(current)) {
                    sumX += entries.get(j).x;
                    count++;
                    sumY += entries.get(j).y;
                }
            }

            if (count == 0) {
                centroids.add(new Point2D(Math.random(), Math.random()));
            } else {
                centroids.add(new Point2D(sumX / count, sumY / count));
            }
        }

        return centroids;
    }

    private static List<Point2D> getLabels(Dataset dataset, List<Point2D> centroids) {
        List<Point2D> labels = new ArrayList<>();

        List<Point2D> entries = dataset.getEntries();
        for (Point2D entry : entries) {
            labels.add(getClosestCentroid(entry, centroids));
        }

        return labels;
    }

    private static Point2D getClosestCentroid(Point2D point, List<Point2D> centroids) {
        int closest = 0;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < centroids.size(); i++) {
            double distance = Math.sqrt(Math.pow((point.x - centroids.get(i).x), 2) +
                    Math.pow((point.y - centroids.get(i).y), 2));
            if (distance < minDistance) {
                minDistance = distance;
                closest = i;
            }
        }

        temp_min_distance = minDistance;
        return centroids.get(closest);
    }

    private static boolean shouldStop(List<Point2D> oldCentroids, List<Point2D> centroids, int iterations) {
        if (iterations > MAX_ITERATIONS) {
            return true;
        }

        if (oldCentroids == null) {
            return false;
        }

        return oldCentroids.equals(centroids);
    }

    private static List<Point2D> getRandomCentroids(Dataset dataset, int centroidsCounts) {
        List<Point2D> centroids = new ArrayList<>();

        Random rand = new Random();

        for (int i = 0; i < centroidsCounts; i++) {

            Point2D point2D = dataset.getEntries().get(rand.nextInt(dataset.getEntries().size()));

            centroids.add(new Point2D(point2D));
        }

        return centroids;
    }
}
