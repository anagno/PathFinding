/**
 * 
 */
package pathfinding;

import java.awt.Point; //for representing the points
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * @author anagno
 *
 */
public class AStar
{
  
  public AStar(Point start, Point goal, Map map)
  {
    start_ = start;
    goal_ = goal;
    map_ = new AStarNode[map.getWidth()][map.getHeight()];
    
    for (int i=0; i<map.getWidth(); ++i)
    {
      for (int j=0; j<map.getHeight(); ++j)
      {
        Point current_point = new Point(i,j);
        map_[i][j] = new AStarNode(map.getNode(current_point));
      }
    }
  }
  
  public LinkedList<Node> findPath()
  {
    //System.out.println("Creating open_list and close_list");
    open_list_ = new LinkedList<AStarNode>();
    closed_list_ = new LinkedList<AStarNode>();
    
    
    AStarNode current, start = new AStarNode(start_,false), goal = new AStarNode(goal_,false);
    
    //System.out.println("start node: " + start.toString() + "\n");
    //System.out.println("goal node: " + goal.toString() + "\n");
    
    start.setGScore(0); // setting  the g score of the start to zero
    
    start.setFScore(calculateDistance(start,goal));
    
    open_list_.add(start); // add starting node to open list
    //System.out.println("open list in the start: " + open_list_.toString() + "\n");
    
    while(!open_list_.isEmpty())
    {
      current = lookingForBestNode(); // get node with lowest f Costs from open list
      System.out.println("current node: " + current.toString() + "\n");
      
      if (current.equals(goal)) // found goal
        return constructPath(current);
      
      open_list_.remove(current); // delete current node from open list
      closed_list_.add(current); // add current node to closed list
      
      //TODO Να δω μήπως και χρησιμοποιήσω την συνάρτηση από την κλάση Map
      LinkedList<AStarNode> adjacent_nodes= new LinkedList<AStarNode>();
       
      int current_x = current.getPosition().x;
      int current_y = current.getPosition().y;
      

      for (int left_top_y=current_y-1; left_top_y <current_y+2; ++left_top_y)
      {
        for (int left_top_x=current_x-1; left_top_x <current_x+2; ++left_top_x)
        {
          try
          {
            AStarNode current_adj = map_[left_top_x][left_top_y];
            System.out.println("Inside ajdacent \n");
            
            if (current_adj.isObstacle() || current_adj.equals(current) || closed_list_.contains(current_adj))
            {
              continue;
            }
            else
            {
              adjacent_nodes.add(current_adj);
            }
          }
          catch (ArrayIndexOutOfBoundsException e)
          {
           //do nothing 
          }
        }
      }
           
      for (int idx = 0, size = adjacent_nodes.size(); idx<size; ++idx)
      {
        AStarNode current_adj = adjacent_nodes.get(idx);
        if (!open_list_.contains(current_adj)) // node is not in open list
        {
          current_adj.setParentNode(current); // set current node as previous for this node
          current_adj.setGScore(current, COST); // set g costs of this node (costs from start to this node)
          current_adj.setFScore(calculateDistance(current_adj,goal));
          open_list_.add(current_adj);
        }
        else // node is in open list
        {
          // costs from current node are cheaper than previous costs
          if(current_adj.getGScore()> (current.getGScore() + COST)) 
          {
            current_adj.setParentNode(current); // set current node as previous for this node
            current_adj.setGScore(current.getGScore() + COST); // set g costs of this node (costs from start to this node)
          }
        }
      }
      
      
    }
    
    return null;
    
  }
  
  private double calculateDistance (AStarNode start, AStarNode finish)
  {
    return Math.sqrt(Math.pow(start.getPosition().x - finish.getPosition().x, 2) 
        + Math.pow(start.getPosition().y - finish.getPosition().y, 2));
  }
  
 
  private LinkedList<Node> constructPath (AStarNode goal)
  {
    LinkedList<Node> path = new LinkedList<Node>();
    while ( goal != null)
    {
      path.addFirst(goal.getNode());
      System.out.println("Inside construct paht: goal: " + goal.toString() + "\n");
      goal = goal.getParentNode();
    }
    
    return path;    
  }
  
  private AStarNode lookingForBestNode()
  {
    //TODO Βελτιστοποίηση αναζήτησης
    if (open_list_.isEmpty())
      return null;
    
    double lowest_cost = open_list_.get(0).getFScore();
    int lowest_idx=0;
    // Ξεκινάμε το for από το 1 μιας και έχουμε ήδη ορίσει ότι η καλύτερη τιμή
    // είναι ο πρώτος node
    for (int idx = 1, size = open_list_.size(); idx < size; ++idx)
    {
      AStarNode current_node = open_list_.get(idx);
      if (current_node.getFScore() < lowest_cost )
      {
        lowest_cost = current_node.getFScore();
        lowest_idx = idx;
      }
    }
    
    return open_list_.get(lowest_idx);
  }
  
  // List containing nodes not visited but adjacent to visited nodes.
  private LinkedList<AStarNode> open_list_;
  // List containing nodes already visited/taken care of.
  private LinkedList<AStarNode> closed_list_;
  // The variables for representing the goal point and the start point
  private Point goal_, start_;
  
  
  private AStarNode[][] map_;
  
  private final int COST = 1 ; 
  
  public class AStarNode
  {
    
    public AStarNode(Point position) 
    {
      this(position, false);
    }
    
    public AStarNode(Point position, boolean is_obstacle)
    {
      node_ = new Node(position);
      setParentNode(null);
    }
    
    public AStarNode(Node node) 
    {
      node_=node;
      setParentNode(null);
    }
    
    /**
     * @return the node_
     */
    public Node getNode()
    {
      return node_;
    }
    
    /**
     * @return the position_
     */
    public Point getPosition()
    {
      return node_.getPosition();
    }

    /**
     * @param position_ the position_ to set
     */
    protected void setPosition(Point position)
    {
      node_.setPosition(position); 
    }
    
    /**
     * @return the is_obstacle_
     */
    public boolean isObstacle()
    {
      return node_.isObstacle();
    }

    /**
     * @param is_obstacle_ the is_obstacle_ to set
     */
    protected void setAsObstacle(ArrayList<Node> edjes)
    {
      node_.setAsObstacle(edjes); 
    }
    
    /**
     * @param is_obstacle_ the is_obstacle_ to set
     */
    protected void setAsObstacle()
    {
      node_.setAsObstacle();    
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
     * returns weather the coordinates of Nodes are equal.
     *
     * @param obj
     * @return
     */
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
      AStarNode other = (AStarNode) obj;
      if ( other.getNode().getPosition().x  == node_.getPosition().x && 
           other.getNode().getPosition().y == node_.getPosition().y ) 
      {
        return true;
      }
      
      return false;
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
    
    // A private variable saving the informations regarding the node.
    private Node node_;
    
    // A private variable showing the parent node
    private AStarNode parent_node_;
     
    //Estimated total cost from start to goal through y.
    private double f_score_;
    //Cost from start along best known path.
    private double g_score_;

  }

}
