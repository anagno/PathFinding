/**
 * 
 */
package pathfinding;

import java.awt.Point; //for representing the points

/**
 * @author anagno
 *
 */
public class AbstractNode
{
  
  public AbstractNode(Point position) 
  {
    this(position, false);
  }
  
  public AbstractNode(Point position, boolean is_obstacle)
  {
    setPosition(position);
    setObstacle(is_obstacle);
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
   * @param is_obstacle_ the is_obstacle_ to set
   */
  protected void setObstacle(boolean is_obstacle)
  {
    is_obstacle_ = is_obstacle;
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
    AbstractNode other = (AbstractNode) obj;
    if ( other.getPosition().x == getPosition().x && other.getPosition().y == getPosition().y ) 
    {
      return true;
    }
    
    return false;
  }
  
  // A private variable showind the location of the node
  private Point position_; 
  
  // A private variable showing if the cell is obstacle
  private boolean is_obstacle_;

}
