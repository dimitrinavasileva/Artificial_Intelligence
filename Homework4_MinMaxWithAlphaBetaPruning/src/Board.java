import java.util.HashSet;

public class Board {
    private PlayerSign[][] board;
    private final int BOARD_SIZE;
    private PlayerSign playersTurn;
    private PlayerSign winner;
    private int movesCount;
    private boolean isGameOver;
    private HashSet<Integer> movesAvailable;

    public Board() {
        BOARD_SIZE = 3;
        playersTurn = PlayerSign.EMPTY;
        winner = PlayerSign.EMPTY;
        movesCount = 0;
        isGameOver = false;
        movesAvailable = new HashSet<>();
        initializeBoard();
    }

    public PlayerSign getPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(PlayerSign playersTurn) {
        this.playersTurn = playersTurn;
    }

    public HashSet<Integer> getAvailableMoves() {
        return movesAvailable;
    }

    public PlayerSign getWinner() {
        return winner;
    }

    public Board copy() {
        Board board = new Board();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.arraycopy(this.board[i], 0, board.board[i], 0, BOARD_SIZE);
        }

        board.playersTurn = this.playersTurn;
        board.winner = this.winner;
        board.movesAvailable = new HashSet<>();
        board.movesAvailable.addAll(this.movesAvailable);
        board.movesCount = this.movesCount;
        board.isGameOver = this.isGameOver;

        return board;
    }

    private void initializeBoard() {
        board = new PlayerSign[BOARD_SIZE][BOARD_SIZE];

        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                board[i][j] = PlayerSign.EMPTY;
            }
        }

        movesAvailable.clear();

        int size = BOARD_SIZE * BOARD_SIZE;

        for (int i = 0; i < size; i++) {
            movesAvailable.add(i);
        }
    }

    public void move(int index) {
        makeMove(index / BOARD_SIZE, index % BOARD_SIZE);
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col] != PlayerSign.EMPTY) {
            System.out.println();
            System.out.println("This position is not empty! Please choose another position.");
            return false;
        }

        board[row][col] = playersTurn;
        ++movesCount;
        movesAvailable.remove(row * BOARD_SIZE + col);

        isGameOver = isWinner();

        playersTurn = PlayerSign.X_PLAYER == playersTurn ? PlayerSign.O_PLAYER : PlayerSign.X_PLAYER;

        return true;
    }

    public boolean isGameOver() {
        return isGameOver;
    }

    private boolean isWinner() {
        for (int i = 0; i < BOARD_SIZE; ++i) {
            if (board[0][i] == board[1][i] && board[0][i] == board[2][i] && board[0][i] != PlayerSign.EMPTY) {
                winner = board[0][i];
                return true;
            }

            if (board[i][0] == board[i][1] && board[i][0] == board[i][2] && board[i][0] != PlayerSign.EMPTY) {
                winner = board[i][0];
                return true;
            }
        }

        if (board[0][0] == board[1][1] && board[0][0] == board[2][2] && board[0][0] != PlayerSign.EMPTY) {
            winner = board[0][0];
            return true;
        }

        if (board[0][2] == board[1][1] && board[0][2] == board[2][0] && board[0][2] != PlayerSign.EMPTY) {
            winner = board[0][2];
            return true;
        }

        return movesCount == BOARD_SIZE * BOARD_SIZE && winner == PlayerSign.EMPTY;
    }

    public Character getPlayerSymbol(PlayerSign playerSign) {
        if (playerSign == PlayerSign.EMPTY) {
            return PlayerSign.EMPTY.getPlayerSign();
        }

        return playerSign == PlayerSign.X_PLAYER ? PlayerSign.X_PLAYER.getPlayerSign() : PlayerSign.O_PLAYER.getPlayerSign();
    }

    public void printBoard() {
        for (int i = 0; i < BOARD_SIZE; ++i) {
            for (int j = 0; j < BOARD_SIZE; ++j) {
                System.out.print(getPlayerSymbol(board[i][j]) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}