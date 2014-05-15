/*************************************************************************
 *  Compilation:  javac RangeSearchVisualizer.java
 *  Execution:    java RangeSearchVisualizer input.txt
 *  Dependencies: PointSET.java KdTree.java Point2D.java RectHV.java
 *                StdDraw.java In.java
 *
 *  Read points from a file (specified as a command-line arugment) and
 *  draw to standard draw. Also draw all of the points in the rectangle
 *  the user selects by dragging the mouse.
 *
 *  The range search results using the brute-force algorithm are drawn
 *  in red; the results using the kd-tree algorithms are drawn in blue.
 *
 *************************************************************************/

import java.util.ArrayList;

public class RangeSearchVisualizer {

    private static ArrayList<Point2D> points;
    private static KdTree kdtree;
    

    public static void main(String[] args) {

        String filename = args[0];
        In in = new In(filename);


        StdDraw.show(0);

        // initialize the data structures with N points from standard input
        PointSET brute = new PointSET();
        kdtree = new KdTree();
        points = new ArrayList<Point2D>();

        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            //kdtree.insert(p);
            points.add(p);
            brute.insert(p);
        }

        buildTree(points,true);        

        double x0 = 0.0, y0 = 0.0;      // initial endpoint of rectangle
        double x1 = 0.0, y1 = 0.0;      // current location of mouse
        boolean isDragging = false;     // is the user dragging a rectangle
        boolean paintMe = false;


        // draw the points
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(.01);
        brute.draw();

        while (true) {
            StdDraw.show(40);

            // user starts to drag a rectangle
            if (StdDraw.mousePressed() && !isDragging) {
                x0 = StdDraw.mouseX();
                y0 = StdDraw.mouseY();
                isDragging = true;
                paintMe = false;
                //continue;
            }

            // user is dragging a rectangle
            else if (StdDraw.mousePressed() && isDragging) {
                x1 = StdDraw.mouseX();
                y1 = StdDraw.mouseY();
                paintMe = true;
                //continue;
            }

            // mouse no longer pressed
            else if (!StdDraw.mousePressed() && isDragging) {
                isDragging = false;
                paintMe = false;
            }

            if((x0-x1)*(y0-y1) != 0 && paintMe){
                RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                         Math.max(x0, x1), Math.max(y0, y1));
                // draw the points
                StdDraw.clear();
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius(.01);
                brute.draw();

                // draw the rectangle
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.setPenRadius();
                rect.draw();

                // draw the range search results for brute-force data structure in red
                StdDraw.setPenRadius(.03);
                StdDraw.setPenColor(StdDraw.RED);

                long startTime, endTime;
                startTime = System.nanoTime();

                for (Point2D p : brute.range(rect))
                    p.draw();

                endTime = System.nanoTime();
                System.out.printf("Time Taken by Brute Force %d \n", endTime - startTime); 
                   
                // draw the range search results for kd-tree in blue
                StdDraw.setPenRadius(.02);
                StdDraw.setPenColor(StdDraw.BLUE);

                startTime = System.nanoTime();

                for (Point2D p : kdtree.range(rect))
                    p.draw();

                endTime = System.nanoTime();
                System.out.printf("Time Taken by KDTree %d \n", endTime - startTime); 

                StdDraw.show(40);
            }
        }
    }



    public static void buildTree(ArrayList<Point2D> points, boolean xysort) {
        //if xy sort = 1, sort by x
        //otherwise sort by y

        ArrayList<Point2D> sortedPoints;
        if(points.size() > 2) {
            if(xysort) {
                sortedPoints = sortMeByX(points);
                xysort = false;
            }else {
                sortedPoints = sortMeByY(points);
                xysort = true;
            }

            int size = sortedPoints.size();
            int median = size/2;

            kdtree.insert(sortedPoints.get(median));
            //kdtree.draw();
            //StdDraw.show(t);

            ArrayList<Point2D> right = new ArrayList<Point2D>(sortedPoints.subList(median+1,size));
            ArrayList<Point2D> left = new ArrayList<Point2D>(sortedPoints.subList(0,median));

            buildTree(right,xysort);
            buildTree(left,xysort);
        }else{
            for(Point2D p : points ) {
                kdtree.insert(p);
                //kdtree.draw();
                //StdDraw.show(t);
            }
        }


    }

    public static ArrayList<Point2D> sortMeByX(ArrayList<Point2D> points){
        int n = points.size();
        int c, d;
        Point2D swap;

        for (c = 0; c < ( n - 1 ); c++) {
          for (d = 0; d < n - c - 1; d++) {
            if (points.get(d).x() > points.get(d+1).x()) {
              swap          = points.get(d);
              points.set(d, points.get(d+1));
              points.set((d+1), swap);
            }
          }
        }
        return points;
    }


    public static ArrayList<Point2D> sortMeByY(ArrayList<Point2D> points){
        int n = points.size();
        int c, d;
        Point2D swap;

        for (c = 0; c < ( n - 1 ); c++) {
          for (d = 0; d < n - c - 1; d++) {
            if (points.get(d).y() > points.get(d+1).y()) {
              swap          = points.get(d);
              points.set(d, points.get(d+1));
              points.set((d+1), swap);
            }
          }
        }

        return points;
    }
}
