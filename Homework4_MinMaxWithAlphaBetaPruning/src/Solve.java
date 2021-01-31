import java.util.Random;
import java.util.Scanner;

public class Solve {

    public static void main(String[] args) {
        Board board = new Board();
        AlphaBetaPruning play = new AlphaBetaPruning();
        Scanner input = new Scanner(System.in);

        System.out.print("Select your player sign(X or O): ");
        String player = input.nextLine();
        PlayerSign human = player.equals("X") ? PlayerSign.X_PLAYER : PlayerSign.O_PLAYER;
        board.setPlayersTurn(human);
        System.out.println();

        System.out.print("Who will be first(X or O)? ");
        String first = input.nextLine();

        Random random = new Random();

        if (!first.equals(player)) {
            board.setPlayersTurn(human == PlayerSign.X_PLAYER ? PlayerSign.O_PLAYER : PlayerSign.X_PLAYER);
            board.makeMove(random.nextInt(3), random.nextInt(3));
        }

        do {
            board.printBoard();

            System.out.print("Choose your board position by entering row and column: ");
            System.out.println();
            String[] userTurn = input.nextLine().split(" ");
            boolean moveMade = board.makeMove(Integer.parseInt(userTurn[0]), Integer.parseInt(userTurn[1]));
            board.printBoard();

            while (!moveMade) {
                System.out.print("Choose your board position by entering row and column: ");
                System.out.println();
                userTurn = input.nextLine().split(" ");
                moveMade = board.makeMove(Integer.parseInt(userTurn[0]), Integer.parseInt(userTurn[1]));
                board.printBoard();
            }

            System.out.println("Opponent: ");
            play.startABP(board, board.getPlayersTurn(), 0);

        } while (!board.isGameOver());

        board.printBoard();

        if (board.getPlayerSymbol(board.getWinner()) == '_') {
            System.out.println("There is no winner.");
        } else {
            System.out.printf("%s is the winner! %n", board.getPlayerSymbol(board.getWinner()));
        }
    }

}