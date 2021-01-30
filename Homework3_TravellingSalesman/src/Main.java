import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Number of cities: ");
        int n_cities = scanner.nextInt();

        GeneticAlgorithm ga = new GeneticAlgorithm(n_cities);
    }
}
