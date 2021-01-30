import java.util.Comparator;

public class PathComparator implements Comparator<Path> {

    @Override
    public int compare(Path o1, Path o2) {
        return Double.compare(o1.getFitness(), o2.getFitness());
    }
}
