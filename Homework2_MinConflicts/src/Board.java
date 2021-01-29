import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Board {
    private int numberOfQueens;
    private Random random = new Random();

    private int[] queens;
    private int[] row;
    private int[] mainDiagonal;
    private int[] secondaryDiagonal;

    public Board(int numberOfQueens) {
        this.numberOfQueens = numberOfQueens;
        queens = new int[numberOfQueens];
        row = new int[numberOfQueens];
        mainDiagonal = new int[2 * numberOfQueens - 1];
        secondaryDiagonal = new int[2 * numberOfQueens - 1];
        initializeBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < 2 * numberOfQueens - 1; i++) {
            if (i < numberOfQueens) {
                row[i] = 0;
            }

            mainDiagonal[i] = 0;
            secondaryDiagonal[i] = 0;
        }

        for (int col = 0; col < numberOfQueens; col++) {
            queens[col] = random.nextInt(numberOfQueens);

            row[queens[col]] += 1;
            addToMainDiagonal(queens[col], col, 1);
            addToSecondaryDiagonal(queens[col], col, 1);
        }
    }

    private void addToMainDiagonal(int row, int col, int value) {
        mainDiagonal[(numberOfQueens - 1) - (col - row)] += value;
    }

    private void addToSecondaryDiagonal(int row, int col, int value) {
        secondaryDiagonal[row + col] += value;
    }

    private int countConflicts(int row, int col) {
        int mainDiagonalIndex;

        if (row <= col) {
            mainDiagonalIndex = (numberOfQueens - 1) - Math.abs(row - col);
        } else {
            mainDiagonalIndex = (numberOfQueens - 1) + Math.abs(row - col);
        }

        int secondaryDiagonalIndex = row + col;

        return this.row[row] + mainDiagonal[mainDiagonalIndex] + secondaryDiagonal[secondaryDiagonalIndex];
    }

    void solve() {
        ArrayList<Integer> b = new ArrayList<>();

        while (true) {
            int maxConflicts = 0;

            b.clear();

            int QUEEN_CONFLICTS_WITH_HERSELF = 3;
            for (int col = 0; col < numberOfQueens; col++) {
                int numberOfConflicts = countConflicts(queens[col], col) - QUEEN_CONFLICTS_WITH_HERSELF;
                if (numberOfConflicts == maxConflicts) {
                    b.add(col);
                } else if (numberOfConflicts > maxConflicts) {
                    maxConflicts = numberOfConflicts;

                    b.clear();

                    b.add(col);
                }
            }

            if (maxConflicts == 0) {
                return;
            }

            int randomQueen = b.get(random.nextInt(b.size()));
            b.clear();

            int minNumberOfConflicts = numberOfQueens;
            for (int row = 0; row < numberOfQueens; row++) {
                int numberOfConflicts = countConflicts(row, randomQueen);
                if(row == randomQueen) {
                    numberOfConflicts -= QUEEN_CONFLICTS_WITH_HERSELF;
                }
                if (numberOfConflicts == minNumberOfConflicts) {
                    b.add(row);
                } else if (numberOfConflicts < minNumberOfConflicts) {
                    minNumberOfConflicts = numberOfConflicts;

                    b.clear();

                    b.add(row);
                }
            }

            int oldRow = queens[randomQueen];
            int newRow = b.get(random.nextInt(b.size()));
            queens[randomQueen] = newRow;

            row[oldRow] -= 1;
            addToMainDiagonal(oldRow, randomQueen, -1);
            addToSecondaryDiagonal(oldRow, randomQueen, -1);

            row[newRow] += 1;
            addToMainDiagonal(newRow, randomQueen, 1);
            addToSecondaryDiagonal(newRow, randomQueen, 1);
        }
    }

    void print() {
        for (int row = 0; row < numberOfQueens; row++) {
            for (int col = 0; col < numberOfQueens; col++) {
                if (queens[row] == col) {
                    System.out.print("* ");
                } else {
                    System.out.print("- ");
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        System.out.println(" ");

        Scanner sc = new Scanner(System.in);
        System.out.print("Please enter number of queens: ");
        int numberOfQueens = sc.nextInt();
        System.out.println(" ");
        sc.close();

        if (numberOfQueens == 2 || numberOfQueens == 3) {
            throw new IllegalArgumentException("Task has no solution. Queens should not be 2 or 3.");
        }

        Board board = new Board(numberOfQueens);

        long start = System.currentTimeMillis();
        board.solve();
        long stop = System.currentTimeMillis();
        System.out.println("Solution found in " + ((double) (stop - start)) / 1000 + "s.");
        System.out.println(" ");

        System.out.println("The board where * is queen and _ is empty tile:");
        //board.print();
    }
}
