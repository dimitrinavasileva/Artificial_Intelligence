public class Point {
    int x;
    int y;

    public Point() {
        x = 0;
        y = 0;
    }

    public Point(int x1, int y1) {
        x = x1;
        y = y1;
    }

    public Point(Point p) {
        x = p.x;
        y = p.y;
    }

    public double distance(Point p) {
        return Math.sqrt((x - p.x) * (x - p.x) + (y - p.y) * (y - p.y));
    }

    public void print() {
        System.out.println(x + " " + y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Point point = (Point) o;

        return x == point.x && y == point.y;
    }

}
