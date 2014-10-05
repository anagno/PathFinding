/**
 * 
 */
package pathfinding;

import java.awt.Point; //for representing the points
import java.util.ArrayList;

/**
 * @author anagno
 *
 */
public class Node
{
  
  public Node(Point position) 
  {
    setPosition(position);
    setAsObstacle();

  }
  
  public Node(Point position, ArrayList<Node> edges)
  {
    setPosition(position);
    setAsObstacle(edges);
  }
    
  /**
   * @return the edjes_
   */
  public ArrayList<Node> getEdges()
  {
    return edges_;
  }
  
  /**
   * @param is_obstacle_ the is_obstacle_ to set
   */
  protected void setAsObstacle(ArrayList<Node> edges)
  {
    is_obstacle_ = false;
    edges_ = edges;
  }
  
  /**
   * @param is_obstacle_ the is_obstacle_ to set
   */
  protected void setAsObstacle()
  {
    is_obstacle_ = true;
    edges_ = null;      
  }
   
  /**
   * @return the position_
   */
  public Point getPosition()
  {
    return position_;
  }

  /**
   * @param position_ the position_ to set
   */
  protected void setPosition(Point position)
  {
    position_ = position;
  }
  
  /**
   * @return the is_obstacle_
   */
  public boolean isObstacle()
  {
    return is_obstacle_;
  }
  
  /**
   * returns weather the coordinates of Nodes are equal.
   *
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj) 
  {
    if (obj == null) 
    {
      return false;
    }
    if (getClass() != obj.getClass()) 
    {
      return false;
    }
    Node other = (Node) obj;
    if ( other.getPosition().x == getPosition().x && other.getPosition().y == getPosition().y ) 
    {
      return true;
    }
    
    return false;
  }
  
  @Override
  public String toString() 
  {
    return "(" + getPosition().x + ", " + getPosition().y + ")";
  }
  
  
  
  // A private variable showing the location of the node
  private Point position_; 
  
  // A private variable showing if the cell is obstacle
  private boolean is_obstacle_;
  
  // A private variable showing the edjes of the current node.
  private ArrayList<Node> edges_;

}
