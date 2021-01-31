public class AlphaBetaPruning {
    private final int NEGATIVE_INFINITY;
    private final int POSITIVE_INFINITY;

    public AlphaBetaPruning() {
        NEGATIVE_INFINITY = Integer.MIN_VALUE;
        POSITIVE_INFINITY = Integer.MAX_VALUE;
    }

    //Alpha beta pruning algorithm implementation
    public int startABP(Board board, PlayerSign playerSign, int currentDepth) {
        currentDepth++;

        if (board.isGameOver()) {
            //Get the result of the winner
            PlayerSign opponent = (playerSign == PlayerSign.X_PLAYER) ? PlayerSign.O_PLAYER : PlayerSign.X_PLAYER;
            int MOVE_OPTIMIZER = 10;

            if (board.getWinner() == playerSign) {
                return MOVE_OPTIMIZER - currentDepth;
            } else if (board.getWinner() == opponent) {
                return -(MOVE_OPTIMIZER - currentDepth);
            } else {
                return 0;
            }
        }

        if (playerSign == board.getPlayersTurn()) {
            return getMax(board, playerSign, NEGATIVE_INFINITY, POSITIVE_INFINITY, currentDepth);
        } else {
            return getMin(board, playerSign, NEGATIVE_INFINITY, POSITIVE_INFINITY, currentDepth);
        }
    }

    private int getMax(Board board, PlayerSign playerSign, int alpha, int beta, int currentDepth) {
        int indexOfBestMove = -1;

        for (Integer move : board.getAvailableMoves()) {
            Board modifiedBoard = board.copy();
            modifiedBoard.move(move);

            int result = startABP(modifiedBoard, playerSign, currentDepth);

            if (result > alpha) {
                alpha = result;
                indexOfBestMove = move;
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
        }

        return alpha;
    }

    private int getMin(Board board, PlayerSign playerSign, int alpha, int beta, int currentDepth) {
        int indexOfBestMove = -1;

        for (Integer move : board.getAvailableMoves()) {
            Board modifiedBoard = board.copy();
            modifiedBoard.move(move);
            int score = startABP(modifiedBoard, playerSign, currentDepth);

            if (score < beta) {
                beta = score;
                indexOfBestMove = move;
            }

            if (alpha >= beta) {
                break;
            }
        }

        if (indexOfBestMove != -1) {
            board.move(indexOfBestMove);
        }

        return beta;
    }
}