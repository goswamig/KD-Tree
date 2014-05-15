/*
 A mutable data type KdTree.java that uses a 2d-tree to implement the same API  
 as PointSET.java

*/
import java.util.*;

public class KdTree 
{
/*
  Every node of tree will have a rectangle and point associated with it.
  so node will have 
  Point2D p;
  RectHV rect;
*/

    // Tree variables
    private int size;    // Size of KDTree
    private Node root;   // Root of KDTree

    //Unit Square, Root will have this square 
    private static final double Xmin = 0.0;
    private static final double Xmax = 1.0;
    private static final double Ymin = 0.0;
    private static final double Ymax = 1.0;

    // Tree structure node 
    private class Node 
    {
        //point in the node  
        private Point2D p;
        //rectangle represented by current node 
        private RectHV rect;

        //Childrens 
        private Node left;
        private Node right;

        //constructor store the point as well rectangle 
        private Node(Point2D value, RectHV inRect) 
        {
            p = value;
            rect = inRect;

            left = null;
            right = null;
        }
    }

    public KdTree()               // construct an empty KDTree
    {
        size = 0;
    }

    public boolean isEmpty()      // is the KDTree empty? 
    {
        return size == 0;
    }

    public int size()             //number of points in the KDTree
    {
        return size;
    }
    
    public void insert(Point2D p)    // add the point p to the KDTree (if it is not already in the KDTree)
    {
        root = BuildKDTree(root, p, Xmin, Ymin, Xmax, Ymax, 0);  
    }

    public boolean contains(Point2D p)  // Does the KDTree contain the point p?          
    {
        return (SearchKDTree(root, p, 0) != null);
    }

   
    private int compare(Point2D a, Point2D b, int level)    //Compare two points w.r.t given level in KDTree  
    {
        int res;
        if (level % 2 == 0)         //even label, compare w.r.t. x
        {                           
            res = new Double(a.x()).compareTo(new Double(b.x()));

            if (res == 0)       //There was a tie 
            {
                return new Double(a.y()).compareTo(new Double(b.y()));
            } 
            else 
            {
                return res;
            }
        } 
        else                     //odd level, compare w.r.t. y
        {
            res = new Double(a.y()).compareTo(new Double(b.y()));

            if (res == 0)       //There was a tie
            {
                return new Double(a.x()).compareTo(new Double(b.x()));
            } 
            else 
            {
                return res;
            }
        }
    }

    private Node BuildKDTree(Node root, Point2D value, double xmin, double ymin, double xmax, double ymax, int level) 
    {
        if (root == null)     //if this is the first node 
        {
            size++;
            return new Node(value, new RectHV(xmin, ymin, xmax, ymax));
        }

        int res = compare(value, root.p, level);

        if (res < 0)      //point should go to the left subtree
        {
            if (level % 2 == 0)     //cut the root's rectangle from vertical 
            {
                root.left = BuildKDTree(root.left, value, xmin, ymin, root.p.x(), ymax, level + 1);
            }  
            else                   //cut the root's rectangle from horizontal 
            {
                root.left = BuildKDTree(root.left, value, xmin, ymin, xmax, root.p.y(), level + 1);
            }
        } 
        else if (res > 0)   //point should go to the  right subtree
        {
            if (level % 2 == 0)    //cut the root's rectangle from vertical
            {
                root.right = BuildKDTree(root.right, value, root.p.x(), ymin, xmax, ymax, level + 1);
            } 
            else                  //cut the root's rectangle from horizontal
            {
                root.right = BuildKDTree(root.right, value, xmin, root.p.y(), xmax, ymax, level + 1);
            }
        }
        return root;
    }

    private Point2D SearchKDTree(Node root, Point2D point, int level) 
    {
        if (root != null) 
        {
            int res = compare(point, root.p, level);
            if (res < 0)   //if point lies left
            {
                return SearchKDTree(root.left, point, level + 1);
            } 
            else if (res > 0)  //if point lies right
            {
                return SearchKDTree(root.right, point, level + 1);
            }  
            else    //you just found the point 
            {
                return root.p;
            }
        }
        //point is not in the tree
        return null;
    }

    
    public void draw()        //Draw KDTree
    {
        //StdDraw.clear();
        drawKDTree(root, 0);
    }

      
    private void drawKDTree(Node root, int level)    //Draw the KDTree inorderly 
    {
        if (root != null) 
        {
            //Draw left subtree
            drawKDTree(root.left, level + 1);
                
            //Draw the line for root 
            StdDraw.setPenRadius();
            if (level % 2 == 0)     //if it is vertical split
            {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(root.p.x(), root.rect.ymin(), root.p.x(), root.rect.ymax());
            } 
            else                   //if it is horizontal split
            {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(root.rect.xmin(), root.p.y(), root.rect.xmax(), root.p.y());
            }

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(.01);
            //draw the point of root
            root.p.draw();

            //draw the right subtree
            drawKDTree(root.right, level + 1);
        }
    }

    // all points in the KDTree that are inside the given rectangle
    public Iterable<Point2D> range(RectHV rect) 
    {
        LinkedList<Point2D> queue = new LinkedList<Point2D>();
        rangeList(root, rect, queue, true);
        return queue;
    }
    //Built a list of all the points lying in the query rectangel 
    private void rangeList(Node root, RectHV rect, LinkedList<Point2D> queue, boolean xysort) {

        //if xy sort = 1, sort by x
        //otherwise sort by y
        if (root != null && rect.intersects(root.rect)) {
            
            if (rect.contains(root.p))   //if root lies inside the query rectangel
            {
                queue.add(root.p);
            }

            if (xysort) {
                xysort = false;
                if(rect.xmax() >= root.p.x() && rect.xmin() <= root.p.x()){
                    rangeList(root.left, rect, queue,xysort);    //search in the left subtree 
                    rangeList(root.right, rect, queue,xysort);   //search in thr right subtree
                } else if(rect.xmax() < root.p.x()){
                    rangeList(root.left, rect, queue,xysort);   //search in thr right subtree
                } else if(rect.xmin() > root.p.x()){
                    rangeList(root.right, rect, queue,xysort);    //search in the left subtree 
                }
            }else {
                xysort = true;
                if(rect.ymax() >= root.p.y() && rect.ymin() <= root.p.y()){
                    rangeList(root.left, rect, queue,xysort);    //search in the left subtree 
                    rangeList(root.right, rect, queue,xysort);   //search in thr right subtree
                } else if(rect.ymax() < root.p.y()){
                    rangeList(root.left, rect, queue,xysort);   //search in thr right subtree
                } else if(rect.ymin() > root.p.y()){
                    rangeList(root.right, rect, queue,xysort);    //search in the left subtree 
                }
            }

            //rangeList(root.left, rect, queue);    //search in the left subtree 
            //rangeList(root.right, rect, queue);   //search in thr right subtree
        }
    }
}
