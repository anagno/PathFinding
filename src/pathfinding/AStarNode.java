/**
 * 
 */
package pathfinding;

import java.awt.Point; //for representin the points

/**
 * @author anagno
 *
 */
public class AStarNode extends AbstractNode
{
  
  public AStarNode(Point position) 
  {
    this(position, false);
  }
  
  public AStarNode(Point position, boolean is_obstacle)
  {
    super(position,is_obstacle);
    setParentNode(null);
  }
  
  /**
   * @return the parent_node_
   */
  public AStarNode getParentNode()
  {
    return parent_node_;
  }

  /**
   * @param parent_node the parent_node_ to set
   */
  public void setParentNode(AStarNode parent_node)
  {
    parent_node_ = parent_node;
  }

  /**
   * @return the f_score_
   */
  public double getFScore()
  {
    return f_score_;
  }

  /**
   * @param f_score the f_score_ to set
   */
  public void setFScore(double f_score)
  {
    f_score_ = f_score;
  }

  /**
   * @return the g_score_
   */
  public double getGScore()
  {
    return g_score_;
  }

  /**
   * @param g_score the g_score_ to set
   */
  public void setGScore(double g_score)
  {
    g_score_ = g_score;
  }
  
  public void setGScore(AStarNode previous_node,double g_score)
  {
    setGScore(previous_node.getGScore() + g_score);
  }
  
  /**
   * returns a String containing the coordinates, as well as h, f and g
   * costs.
   *
   * @return
   */
  @Override
  public String toString() 
  {
    return "(" + getPosition().x + ", " + getPosition().y + "): g: " + getGScore() + " f: " + getFScore();
  }
  
  // A private variable showing the parent node
  private AStarNode parent_node_;
   
  //Estimated total cost from start to goal through y.
  private double f_score_;
  //Cost from start along best known path.
  private double g_score_;

}
