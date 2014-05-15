/*************************************************************************
 *  Compilation:  javac KdTreeVisualizer.java
 *  Execution:    java KdTreeVisualizer
 *  Dependencies: StdDraw.java Point2D.java KdTreeST.java
 *
 *  Add the points that the user clicks in the standard draw window
 *  to a kd-tree and draw the resulting kd-tree.
 *
 *************************************************************************/
import java.util.ArrayList;

public class KdTreeVisualizer {

    private static ArrayList<Point2D> points;
    private static KdTree kdtree;
    private static PointSET brute;

    public static void main(String[] args) {
        
        boolean treeBuilt = true;
        boolean isDragging = false;
        boolean paintMe = false;
        boolean done = false;

        long startTime, endTime;


        double x0 = 0.0;
        double x1 = 0.0;
        double y0 = 0.0;
        double y1 = 0.0;

        points = new ArrayList<Point2D>();
        kdtree = new KdTree();
        brute = new PointSET();
        while (treeBuilt) {
            StdDraw.setPenRadius(.005);
            if (StdDraw.mousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                System.out.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                p.draw();
                points.add(p);
                brute.insert(p);
                //kdtree.insert(p);
                //StdDraw.clear();
                //kdtree.draw();
            }
            if (StdDraw.hasNextKeyTyped()) {
                buildTree(points,true, 100);
                treeBuilt = false;
            }
            StdDraw.show(50);
        }


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
                done = true;
            }
            RectHV rect = new RectHV(Math.min(x0, x1), Math.min(y0, y1),
                                         Math.max(x0, x1), Math.max(y0, y1));

            if((x0-x1)*(y0-y1) != 0 && paintMe){
                StdDraw.setPenColor(StdDraw.BLACK);
                StdDraw.clear();
                
                rect.draw();
                kdtree.draw();
                //buildTree(points,true, 0);

                StdDraw.setPenColor(StdDraw.BLUE);
                for (Point2D p : kdtree.range(rect))
                    p.draw();

            }

            if((done) && !(isDragging) && (x0-x1)*(y0-y1) != 0 ){
                //////
                startTime = System.nanoTime();
                Iterable<Point2D> bruteRange = brute.range(rect);
                endTime = System.nanoTime();
                ///////

                System.out.printf("Time Taken by Brute Force %d \n", endTime - startTime); 
                
                
                /////////
                startTime = System.nanoTime();
                Iterable<Point2D> kdRange = kdtree.range(rect);
                endTime = System.nanoTime();
                /////////

                System.out.printf("Time Taken by KDTree %d \n", endTime - startTime); 

                done = false;
            }

        }

    }

    public static void buildTree(ArrayList<Point2D> points, boolean xysort, int t) {
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
            kdtree.draw();
            StdDraw.show(t);

            ArrayList<Point2D> right = new ArrayList<Point2D>(sortedPoints.subList(median+1,size));
            ArrayList<Point2D> left = new ArrayList<Point2D>(sortedPoints.subList(0,median));

            buildTree(right,xysort,t);
            buildTree(left,xysort,t);
        }else{
            for(Point2D p : points ) {
                kdtree.insert(p);
                kdtree.draw();
                StdDraw.show(t);
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


