
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class PointSET {
   private TreeSet<Point2D> points;
   public PointSET()                               // construct an empty set of points
   {
       points =  new TreeSet<Point2D>();
   }
   public boolean isEmpty()                        // is the set empty?
   {
       return points.isEmpty();
   }
   public int size()                               // number of points in the set
   {
       return points.size(); 
   }
   public void insert(Point2D p)                   // add the point p to the set (if it is not already in the set)
   {
       points.add(p);
   }
   public boolean contains(Point2D p)              // does the set contain the point p?
   {
       return points.contains(p);
   }
   public void draw()                              // draw all of the points to standard draw
   {
       for(Point2D p : points )
           p.draw();
   }
   public Iterable<Point2D> range(RectHV rect)     // all points in the set that are inside the rectangle
   {
        Set<Point2D> rangePoints = new HashSet<Point2D>();
        for(Point2D p : points)
           if(rect.contains( p))
              rangePoints.add(p);
 
       return rangePoints;
   }
}
