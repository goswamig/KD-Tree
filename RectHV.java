
/*************************************************************************
 *  Compilation:  javac RectHV.java
 *  Execution:    java RectHV
 *  Dependencies: Point2D.java
 *
 *  Implementation of 2D axis-aligned rectangle.
 *
 *************************************************************************/

public class RectHV {
    private final double xmin, ymin;   // minimum x- and y-coordinates
    private final double xmax, ymax;   // maximum x- and y-coordinates

    // construct the axis-aligned rectangle [xmin, xmax] x [ymin, ymax]
    public RectHV(double xmin, double ymin, double xmax, double ymax) {
//        if (xmax < xmin || ymax < ymin) {
//            throw new IllegalArgumentException("Invalid rectangle");
//        }
        if(xmin > xmax)    //this happens when you generate the points random and insert into the kd tree 
        {
           double tmp;
           tmp = xmax;
           xmax = xmin;
           xmin = tmp;
        }

        if(ymin > ymax)    //this happens when you generate the points random and insert into the kd tree 
        {
           double tmp;
           tmp = ymax;
           ymax = ymin;
           ymin = tmp;
        }

        this.xmin = xmin;
        this.ymin = ymin;
        this.xmax = xmax;
        this.ymax = ymax;
    }

    // accessor methods for 4 coordinates
    public double xmin() { return xmin; }
    public double ymin() { return ymin; }
    public double xmax() { return xmax; }
    public double ymax() { return ymax; }

    // width and height of rectangle
    public double width()  { return xmax - xmin; }
    public double height() { return ymax - ymin; }

    // does this axis-aligned rectangle intersect that one?
    public boolean intersects(RectHV that) {
        return this.xmax >= that.xmin && this.ymax >= that.ymin
            && that.xmax >= this.xmin && that.ymax >= this.ymin;
    }

    // draw this axis-aligned rectangle
    public void draw() {
        StdDraw.line(xmin, ymin, xmax, ymin);
        StdDraw.line(xmax, ymin, xmax, ymax);
        StdDraw.line(xmax, ymax, xmin, ymax);
        StdDraw.line(xmin, ymax, xmin, ymin);
    }

    // distance from p to closest point on this axis-aligned rectangle
    public double distanceTo(Point2D p) {
        return Math.sqrt(this.distanceSquaredTo(p));
    }

    // distance squared from p to closest point on this axis-aligned rectangle
    public double distanceSquaredTo(Point2D p) {
        double dx = 0.0, dy = 0.0;
        if      (p.x() < xmin) dx = p.x() - xmin;
        else if (p.x() > xmax) dx = p.x() - xmax;
        if      (p.y() < ymin) dy = p.y() - ymin;
        else if (p.y() > ymax) dy = p.y() - ymax;
        return dx*dx + dy*dy;
    }

    // does this axis-aligned rectangle contain p?
    public boolean contains(Point2D p) {
        return (p.x() >= xmin) && (p.x() <= xmax)
            && (p.y() >= ymin) && (p.y() <= ymax);
    }

    // are the two axis-aligned rectangles equal?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        RectHV that = (RectHV) y;
        if (this.xmin != that.xmin) return false;
        if (this.ymin != that.ymin) return false;
        if (this.xmax != that.xmax) return false;
        if (this.ymax != that.ymax) return false;
        return true;
    }

    // return a string representation of this axis-aligned rectangle
    public String toString() {
        return "[" + xmin + ", " + xmax + "] x [" + ymin + ", " + ymax + "]";
    }


    /**
     * Unit tests the point data type.
     */
    public static void main(String[] args) {
       // int x0 = Integer.parseInt(args[0]);
       // int y0 = Integer.parseInt(args[1]);
        int N = Integer.parseInt(args[0]);   //draw these many rectangales

        StdDraw.setCanvasSize(800, 800);
        StdDraw.setXscale(0, 100);
        StdDraw.setYscale(0, 100);
        StdDraw.setPenRadius(.005);
        RectHV[] rects = new RectHV[N];
        for (int i = 0; i < N; i++) {
            int x0 = StdRandom.uniform(100);
            int x1 = StdRandom.uniform(100);
            int xmin = x0>x1?x1:x0;
            int xmax = x0>x1?x0:x1;
            int y0 = StdRandom.uniform(100);
            int y1 = StdRandom.uniform(100);
            int ymin = y0>y1?y1:y0;
            int ymax = y0>y1?y0:y1;

            rects[i] = new RectHV(xmin, ymin, xmax, ymax);
            rects[i].draw();
        }
/*
        // draw p = (x0, x1) in red
        Point2D p = new Point2D(x0, y0);
        StdDraw.setPenColor(StdDraw.RED);
        StdDraw.setPenRadius(.02);
        p.draw();


        // draw line segments from p to each point, one at a time, in polar order
        StdDraw.setPenRadius();
        StdDraw.setPenColor(StdDraw.BLUE);
        Arrays.sort(points, p.POLAR_ORDER);
        for (int i = 0; i < N; i++) {
            p.drawTo(points[i]);
            StdDraw.show(100);
        } */
    }

}


