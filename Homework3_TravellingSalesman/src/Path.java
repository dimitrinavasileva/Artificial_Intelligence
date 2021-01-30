import java.util.ArrayList;
import java.util.Random;

public class Path {
    private final static int MIN_X = 0;
    private final static int MIN_Y = 0;
    private final static int COORDINATES_SIZE = 100;
    private final static int MUTATION_CHANCE_PERCENT = 1;

    private final Point[] path;
    private final int length;
    private double fitness;
    private double pathLength;

    Random rnd = new Random();

    public Path(int n_cities) {
        //Creating path of random points
        length = n_cities;
        path = new Point[length];

        for (int i = 0; i < length; i++) {
            path[i] = new Point(rnd.nextInt(COORDINATES_SIZE) + MIN_X, rnd.nextInt(COORDINATES_SIZE) + MIN_Y);
        }

        calculateFitness();
    }

    public Path(Point[] p) {
        length = p.length;
        path = new Point[length];

        for (int i = 0; i < length; i++) {
            path[i] = new Point(p[i]);
        }

        calculateFitness();
    }

    public Path shufflePath() {
        //Copying array
        Point[] arr = new Point[path.length];
        System.arraycopy(path, 0, arr, 0, length);

        int index;
        Point temp;
        //Shuffling the copied array
        for (int i = arr.length - 1; i > 0; i--) {
            index = rnd.nextInt(i + 1);
            temp = arr[index];
            arr[index] = arr[i];
            arr[i] = temp;
        }

        return new Path(arr);
    }

    private void calculateFitness() {
        fitness = 0;

        for (int i = 0; i < length - 1; i++) {
            fitness += path[i].distance(path[i + 1]);
        }

        pathLength = fitness;
        fitness = 1 / (fitness + 1); //Inverting the fitness, higher sum distance represents lower fitness
    }

    //One point crossover
    public Path crossOver(Path p) {
        //Picking random position for the crossover
        int n = rnd.nextInt(this.length) + 1;
        if (n >= length - 1) n = length - 2;

        //Point[] arr = new Point[length];
        ArrayList<Point> arrList = new ArrayList<>();

        //Copying first part
        for (int i = 0; i < n; i++) {
            arrList.add(new Point(this.getCity(i)));
        }

        //Copying second part
        int iter = 0;
        for (int i = n; i < length; i++) {
            for (; iter < length; iter++) {
                if (!arrList.contains(p.getCity(iter))) {
                    arrList.add(p.getCity(iter));
                }
            }
        }

        Point[] arr = arrList.toArray(new Point[arrList.size()]);

        //Mutation chance
        if (rnd.nextInt(100) < MUTATION_CHANCE_PERCENT) {
            randomSwap(arr);
        }

        return new Path(arr);
    }

    public void randomSwap(Point[] arr) {
        int i = rnd.nextInt(arr.length);
        int j = rnd.nextInt(arr.length);

        Point temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    public Point getCity(int n) {
        if (n < length) {
            return path[n];
        }

        return new Point();
    }

    public double getFitness() {
        return fitness;
    }

    public double getPathLength() {
        return pathLength;
    }
}
