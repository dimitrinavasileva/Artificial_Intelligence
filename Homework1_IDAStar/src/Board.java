import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.System.exit;

public class Board {
    private int[][] board;
    private int boardTiles;
    private int dimension;
    private int zeroPosition;
    private int goalZeroPosition;
    private int manhattanDistance;
    private int g;
    private Move previousMove;
    private Board previousBoard;
    private int zeroRow;
    private int zeroColumn;
    private ArrayList<Board> boardChildren;

    public Board(int[][] board, int boardTiles, int goalZeroPosition) {
        setBoardTiles(boardTiles);
        setDimension();
        setBoard(board);
        isSolvable();
        setGoalZeroPosition(goalZeroPosition);
        this.manhattanDistance = manhattanDistance();
        this.g = 0;
        this.previousMove = null;
    }

    public void setBoardTiles(int boardTiles) {
        if (boardTiles > 0) {
            this.boardTiles = boardTiles;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void setDimension() {
        this.dimension = (int) Math.sqrt(boardTiles + 1);
    }

    public void setGoalZeroPosition(int goalZeroPosition) {
        if (goalZeroPosition == -1) {
            this.goalZeroPosition = boardTiles;
        } else if (goalZeroPosition >= 0 && boardTiles >= goalZeroPosition) {
            this.goalZeroPosition = goalZeroPosition;
        } else {
            throw new IllegalArgumentException("Illegal value for the zero goal position.");
        }
    }

    public void setBoard(int[][] board) {
        if (board == null) {
            throw new IllegalArgumentException();
        }

        this.board = new int[dimension][dimension];

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                this.board[i][j] = board[i][j];
                if (board[i][j] == 0) {
                    zeroPosition = i * dimension + j;
                }
            }
        }
    }

    public void isSolvable() {
        boolean solvable = solvable();

        if (!solvable) {
            System.out.println("This board is unsolvable. Please enter again.");
            exit(1);
        }
    }

    private boolean solvable() {
        int inversions = 0;

        for (int i = 0; i < dimension * dimension; i++) {
            int currentRow = i / dimension;
            int currentColumn = i % dimension;

            if (tileAt(currentRow, currentColumn) == 0) {
                this.zeroRow = currentRow;
                this.zeroColumn = currentColumn;
            }

            for (int j = i; j < dimension * dimension; j++) {
                int row = j / dimension;
                int column = j % dimension;


                if (tileAt(row, column) != 0 && tileAt(row, column) < tileAt(currentRow, currentColumn)) {
                    inversions++;
                }
            }
        }

        if (board.length % 2 != 0 && inversions % 2 != 0) {
            return false;
        }

        return board.length % 2 != 0 || (inversions + this.zeroRow) % 2 != 0;
    }

    public int manhattanDistanceOfTile(int row, int col) {
        int tile = board[row][col];

        if (tile == 0) {
            return 0;
        }

        if (tile - 1 >= goalZeroPosition) {
            tile++;
        }

        int goalRow = (tile - 1) / dimension;
        int goalCol = (tile - 1) % dimension;

        return Math.abs(goalRow - row) + Math.abs(goalCol - col);
    }

    public int manhattanDistance() {
        int sum = 0;

        for (int i = 0; i <= dimension - 1; i++) {
            for (int j = 0; j <= dimension - 1; j++) {
                sum += manhattanDistanceOfTile(i, j);
            }
        }

        return sum;
    }

    public int getManhattanDistance() {
        return manhattanDistance;
    }

    public int getG() {
        return g;
    }

    public Move getPreviousMove() {
        return previousMove;
    }

    public Board getPreviousBoard() {
        return previousBoard;
    }

    public void leftChild() {
        if (zeroColumn != dimension - 1) {
            Board left = new Board(board, boardTiles, goalZeroPosition);
            left.board[zeroRow][zeroColumn] = left.board[zeroRow][zeroColumn + 1];
            left.board[zeroRow][zeroColumn + 1] = 0;
            left.manhattanDistance = left.manhattanDistance();
            left.zeroPosition++;
            left.previousMove = Move.LEFT;
            left.previousBoard = this;
            left.g = this.g + 1;
            boardChildren.add(left);
        }
    }

    public void rightChild() {
        if (zeroColumn != 0) {
            Board right = new Board(board, boardTiles, goalZeroPosition);
            right.board[zeroRow][zeroColumn] = right.board[zeroRow][zeroColumn - 1];
            right.board[zeroRow][zeroColumn - 1] = 0;
            right.manhattanDistance = right.manhattanDistance();
            right.zeroPosition--;
            right.previousMove = Move.RIGHT;
            right.previousBoard = this;
            right.g = this.g + 1;
            boardChildren.add(right);
        }
    }

    public void upChild() {
        if (zeroRow != dimension - 1) {
            Board up = new Board(board, boardTiles, goalZeroPosition);
            up.board[zeroRow][zeroColumn] = up.board[zeroRow + 1][zeroColumn];
            up.board[zeroRow + 1][zeroColumn] = 0;
            up.manhattanDistance = up.manhattanDistance();
            up.zeroPosition = up.zeroPosition + dimension;
            up.previousMove = Move.UP;
            up.previousBoard = this;
            up.g = this.g + 1;
            boardChildren.add(up);
        }
    }

    public void downChild() {
        if (zeroRow != 0) {
            Board down = new Board(board, boardTiles, goalZeroPosition);
            down.board[zeroRow][zeroColumn] = down.board[zeroRow - 1][zeroColumn];
            down.board[zeroRow - 1][zeroColumn] = 0;
            down.manhattanDistance = down.manhattanDistance();
            down.zeroPosition = down.zeroPosition - dimension;
            down.previousMove = Move.DOWN;
            down.previousBoard = this;
            down.g = this.g + 1;
            boardChildren.add(down);
        }
    }

    public ArrayList<Board> getBoardChildren() {
        this.boardChildren = new ArrayList<>();
        this.zeroRow = zeroPosition / dimension;
        this.zeroColumn = zeroPosition % dimension;

        leftChild();
        rightChild();
        upChild();
        downChild();
        boardChildren.sort(new BoardComparator());
        return boardChildren;
    }

    public int tileAt(int row, int col) {
        if (row < 0 || row > dimension - 1) {
            throw new IndexOutOfBoundsException("Row should be between 0 and dimension - 1.");
        }

        if (col < 0 || col > dimension - 1) {
            throw new IndexOutOfBoundsException("Column should be between 0 and dimension - 1.");
        }

        return board[row][col];
    }

    public void printBoard() {
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                System.out.print(tileAt(i, j));
                System.out.print(" ");
            }

            System.out.println(" ");
        }
        System.out.println(" ");
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        Board boardObj = (Board) object;
        if (board.length != boardObj.board.length) {
            return false;
        }

        for (int i = 0; i < board.length; i++) {
            if (!Arrays.equals(board[i], boardObj.board[i])) {
                return false;
            }
        }
        return true;
    }
}
