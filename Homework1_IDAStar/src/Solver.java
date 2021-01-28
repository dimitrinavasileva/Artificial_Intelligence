import java.util.*;

public class Solver {

    public Solver(Board board) {
        int i = 0;

        while (!search(board, i)) {
            i++;
        }
    }

    private boolean search(Board board, int border) {
        Stack<Board> stack = new Stack<>();
        stack.push(board);

        while (!stack.isEmpty()) {
            Board currentBoard = stack.pop();

            if (currentBoard.getManhattanDistance() == 0) {
                System.out.println("Board solution:");

                currentBoard.printBoard();
                traceMoves(currentBoard);

                return true;
            }

            int f = currentBoard.getG() + currentBoard.getManhattanDistance();

            if (f <= border) {
                for (Board b : currentBoard.getBoardChildren()) {
                    if (!stack.contains(b)) {
                        stack.push(b);
                    }
                }
            }
        }

        return false;
    }

    private void traceMoves(Board currentBoard) {
        Board current = currentBoard;
        int movementCount = 0;

        Board prev = current.getPreviousBoard();
        List<Move> moves = new ArrayList<>();

        while (current.getPreviousMove() != null) {
            moves.add(current.getPreviousMove());
            current = prev;
            prev = prev.getPreviousBoard();
            movementCount++;
        }

        System.out.println("The length of the \"optimal\" path from the beginning to the goal state is: " + movementCount);
        System.out.println();

        Collections.reverse(moves);

        System.out.println("The steps to reach the goal state are: ");

        for (Move move : moves) {
            System.out.println(move);
        }
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println(" ");
        System.out.print("Please enter number of board tiles(8, 15, 24, etc.): ");
        int boardTiles = sc.nextInt();
        System.out.println();

        System.out.print("Please enter the goal position of the zero (-1 for default): ");
        int zero_goal_pos = sc.nextInt();
        System.out.println();

        System.out.print("Enter board: ");
        System.out.println();
        int dimension = (int) Math.sqrt(boardTiles + 1);
        int[][] inputBoard = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                inputBoard[i][j] = sc.nextInt();
            }
        }

        System.out.println();

        Board board = new Board(inputBoard, boardTiles, zero_goal_pos);

        long start = System.currentTimeMillis();
        new Solver(board);
        long stop = System.currentTimeMillis();

        System.out.println();
        System.out.println("Solution found in " + ((double) (stop - start)) / 1000 + "s.");
        System.out.println();
    }
}
