import java.util.Comparator;

public class BoardComparator implements Comparator<Board> {
    @Override
    public int compare(Board board1, Board board2) {

        int f1 = board1.getG() + board1.getManhattanDistance();
        int f2 = board2.getG() + board2.getManhattanDistance();

        if (f1 < f2) {
            return 1;
        } else if (f1 > f2) {
            return -1;
        }

        return 0;
    }
}
