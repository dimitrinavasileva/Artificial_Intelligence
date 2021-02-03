import dataset.Dataset;
import dataset.Point2D;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main {

    private static final int CLUSTERS_COUNT = 4;
    private static final int RANDOM_RESTARTS = 5;
    private static final String NORMAL_PATH = "Datasets/normal/normal.txt";
    private static final String UNBALANCE_PATH = "Datasets/unbalance/unbalance.txt";
    private static final String IMG_FOLDER_PATH = "img/";

    public static void main(String[] args) {
        run(NORMAL_PATH);
        run(UNBALANCE_PATH);
    }

    private static void run(String fileName) {

        Dataset dataset = Dataset.fromFile(fileName);

        double lowestDistance = Double.MAX_VALUE;
        List<Point2D> bestCentroids = null;

        // executing the algorithm 'restartCount' times and saving the best solution
        // best solution is the one with min sum of distances between the points and the centroids
        for(int i = 0; i < Main.RANDOM_RESTARTS; i++)
        {
            List<Point2D> centroids = KMeansAlgorithm.KMeans(dataset, Main.CLUSTERS_COUNT);

            double distance = KMeansAlgorithm.sum_distance;
            if(distance < lowestDistance)
            {
                lowestDistance = distance;
                bestCentroids = centroids;
            }

        }
        assert bestCentroids != null;

        generateImage(bestCentroids, getImageName(fileName));
    }

    private static String getImageName(String fileName) {
        int index = fileName.lastIndexOf("/");
        String imgNameWithExtension = fileName.substring(index + 1);
        int idx2 = imgNameWithExtension.lastIndexOf(".");
        return IMG_FOLDER_PATH + imgNameWithExtension.substring(0, idx2) + ".png";
    }

    private static void generateImage(List<Point2D> centroids, String imageName) {
        final int imgSize = 1000;
        final int offset = (int) (imgSize * 0.05);

        try {
            BufferedImage bi = new BufferedImage(imgSize, imgSize + offset, BufferedImage.TYPE_INT_ARGB);
            Graphics2D ig2 = bi.createGraphics();

            ig2.setColor(Color.BLACK);
            ig2.drawLine(offset, offset, imgSize, offset);
            ig2.drawLine(offset, offset, offset, imgSize);
            Font font = new Font("TimesRoman", Font.BOLD, 20);
            ig2.setFont(font);
            ig2.drawString("0", offset / 2, (int) (offset * 0.75));


            double maxX = 0;
            double maxY = 0;
            for (Point2D centroid : centroids) {
                if (centroid.x > maxX) {
                    maxX = centroid.x;
                }
                if (centroid.y > maxY) {
                    maxY = centroid.y;
                }
            }
            double scaleX = imgSize / maxX * 0.75;
            double scaleY = imgSize / maxY * 0.75;

            for(int i = 1; i <= Main.CLUSTERS_COUNT; i++) {
                Color c = new Color(Color.HSBtoRGB(Main.CLUSTERS_COUNT / (float) i, 1.0f, 1.0f));
                ig2.setColor(c);

                ig2.fill(new Ellipse2D.Double(centroids.get(i-1).x * scaleX + offset,
                                centroids.get(i-1).y * scaleY + offset,
                                imgSize * 0.03, imgSize * 0.03));
            }

            bi = flipImageHorizontally(bi);
            bi = rotateImage180(bi);

            ImageIO.write(bi, "PNG", new File(imageName));

        } catch (IOException ie) {
            ie.printStackTrace();
        }
    }

    private static BufferedImage rotateImage180(BufferedImage bi) {
        // Flip the image vertically and horizontally; equivalent to rotating the image 180 degrees
        AffineTransform tx = AffineTransform.getScaleInstance(-1, -1);
        tx.translate(-bi.getWidth(null), -bi.getHeight(null));
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(bi, null);
    }

    private static BufferedImage flipImageHorizontally(BufferedImage img) {
        AffineTransform tx = AffineTransform.getScaleInstance(-1, 1);
        tx.translate(-img.getWidth(null), 0);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
        return op.filter(img, null);
    }
}
