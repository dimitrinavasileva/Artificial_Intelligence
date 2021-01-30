import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Random;

public class GeneticAlgorithm {
    private final static int N_ITERATIONS = 100;
    private final static int POP_SIZE = 100;
    private final static int BEST_PERCENTAGE = 10; //What percentage of the best individuals will stay the same next generation
    private final int n_cities;

    private ArrayList<Path> population;
    Random random;

    public GeneticAlgorithm(int n_cities) {
        this.n_cities = n_cities;
        initialize();

        System.out.println();
        int gen = 0;

        for (int i = 0; i < N_ITERATIONS; i++) {
            gen += 1;
            population.sort(new PathComparator());
            if (gen == 10 || gen == N_ITERATIONS / 3 || gen == N_ITERATIONS / 2 || gen == (N_ITERATIONS * 5 / 6) || gen == N_ITERATIONS) {
                String path_length = new DecimalFormat("#0.00").format(population.get(POP_SIZE - 1).getPathLength());
                System.out.println("generation: " + gen + "; path length: " + path_length);
            }

            ArrayList<Path> selected = selection();
            population = crossover(selected);
        }

    }

    public ArrayList<Path> crossover(ArrayList<Path> selected) {
        int i;
        int j;
        ArrayList<Path> children = new ArrayList<>();

        //Adding the best of the current population to the children
        addBest(children);
        int size = selected.size();
        while (children.size() < POP_SIZE) {
            i = random.nextInt(size);
            j = random.nextInt(size);
            if (i != j) {
                children.add(
                        selected.get(i).crossOver(selected.get(j))
                );
            }
        }

        return children;
    }


    private void initialize() {
        random = new Random();
        population = new ArrayList<>();

        //Creating random path with n_cities points
        Path path = new Path(n_cities);
        population.add(path);

        //Initializing the whole population with shuffling of the initial path
        for (int i = 1; i < POP_SIZE; i++) {
            population.add(path.shufflePath());
        }
    }

    private void addBest(ArrayList<Path> arr) {
        //The best individuals are at the end so we get the last BEST_PERCENTAGE elements
        int n = POP_SIZE - POP_SIZE * BEST_PERCENTAGE / 100;
        for (int i = POP_SIZE - 1; i > n; i--) {
            arr.add(population.get(i));
        }
    }

    private ArrayList<Path> selection() {
        ArrayList<Path> selected = new ArrayList<>();

        double minFitness = population.get(0).getFitness();
        double maxFitness = population.get(population.size() - 1).getFitness();

        while (selected.size() < POP_SIZE / 2) {
            for (Path path : population) {
                double r = minFitness + (maxFitness - minFitness) * random.nextDouble();
                if (r <= path.getFitness()) {
                    selected.add(path);
                }
            }
        }

        return selected;
    }
}
