import java.util.Comparator;

public class PathComparator implements Comparator<Path> {
    @Override
    public int compare(Path p1, Path p2) {
        return Integer.compare(p1.getDistance(), p2.getDistance());
    }
}
