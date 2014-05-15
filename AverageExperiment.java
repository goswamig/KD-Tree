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

import java.util.*;


public class AverageExperiment {

    private static ArrayList<Point2D> kdpoints;

    private static KdTree kdtree;
    
    public static void main(String[] args) {

        int N = Integer.parseInt(args[0]); // for how many points

        long iterations = 1;

        long totalBrute = 0;
        long totalKd = 0;

        for (int iter=0; iter < iterations ; iter++) {
        PointSET brute = new PointSET();
        kdtree = new KdTree();
        kdpoints = new ArrayList<Point2D>();

        // Generates N points between 0-1 and intialize the data structures
        Point2D[] points = new Point2D[N];
        for (int i = 0; i < N; i++) {
            double x = StdRandom.uniform(100);
            double y = StdRandom.uniform(100);
            x = x/100;
            y = y/100;
            points[i] = new Point2D(x, y);
            //points[i].draw();
            //System.out.printf("%f %f\n", points[i].x(), points[i].y());
            //kdtree.insert(points[i]);
            
            kdpoints.add(points[i]);
            brute.insert(points[i]);
        }

        buildTree(kdpoints,true); 
        //StdDraw.show();

        //Random rectangle between 0-0.1
        double xmin = StdRandom.uniform(100);
        double ymin = StdRandom.uniform(100);

        xmin = xmin / 100;
        ymin = ymin / 100;

        double xmax = xmin + 0.1;
        double ymax = ymin + 0.1;

        // draw rectangle 
        RectHV rect = new RectHV(xmin, ymin, xmax, ymax);
        //RectHV rect = new RectHV(0.3, 0.4, 0.5, 0.5);   //uncomment this line to see the big rectangle, original rectangle is very small (0-.1)
       
        //////////////////////////////////////////////////
        // draw the range search results for brute-force data structure in red

        long startTime, endTime;
        
        //////
        startTime = System.nanoTime();
        Iterable<Point2D> bruteRange = brute.range(rect);
        endTime = System.nanoTime();
        ///////

        totalBrute += (endTime - startTime); 
        //System.out.printf("Time Taken by Brute Force %d \n", endTime - startTime); 
        
        //StdDraw.clear();
           
        // draw the range search results for kd-tree in blue
        
        /////////
        startTime = System.nanoTime();
        Iterable<Point2D> kdRange = kdtree.range(rect);
        endTime = System.nanoTime();
        /////////
        totalKd += (endTime - startTime); 
        //System.out.printf("Time Taken by KDTree %d \n", endTime - startTime); 
    
        }

        totalKd /= iterations;
        totalBrute /= iterations;

        System.out.printf("Average Time Taken by Brute Force %d \n", totalBrute); 
        System.out.printf("Average Time Taken by KDTree %d \n", totalKd);   
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
            //StdDraw.show();

            ArrayList<Point2D> right = new ArrayList<Point2D>(sortedPoints.subList(median+1,size));
            ArrayList<Point2D> left = new ArrayList<Point2D>(sortedPoints.subList(0,median));

            buildTree(right,xysort);
            buildTree(left,xysort);
        }else{
            for(Point2D p : points ) {
                kdtree.insert(p);
                //kdtree.draw();
                //StdDraw.show();
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