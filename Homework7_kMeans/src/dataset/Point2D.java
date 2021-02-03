package dataset;

import java.util.Objects;

public class Point2D {
    private static final double DELTA = 0.001;

    public double x;
    public double y;

    public Point2D(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Point2D(Point2D o) {
        this.x = o.x;
        this.y = o.y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Point2D point2D = (Point2D) o;
        return Math.abs(point2D.x - this.x) <= DELTA &&
                Math.abs(point2D.y - this.y) <= DELTA;

    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
