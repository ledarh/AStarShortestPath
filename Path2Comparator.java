import java.util.Comparator;

public class Path2Comparator implements Comparator<Path> {
    @Override
    public int compare(Path p1, Path p2) {
        return Integer.compare(p1.getFvalue(), p2.getFvalue());
    }
}
