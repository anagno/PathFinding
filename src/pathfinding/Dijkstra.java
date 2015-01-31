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
public class Dijkstra
{
  
  public Dijkstra(Point start, Point goal, Map map)
  {
    start_ = start;
    goal_ = goal;
    map_ = new DijkstraNode[map.getWidth()][map.getHeight()];
    //Creating open_list and close_list
    open_list_ = new LinkedList<DijkstraNode>();
    
    for (int i=0; i<map.getWidth(); ++i)
    {
      for (int j=0; j<map.getHeight(); ++j)
      {
        Point current_point = new Point(i,j);
        map_[i][j] = new DijkstraNode(map.getNode(current_point));
        open_list_.add(map_[i][j]);
      }
    }
  }
  
  public LinkedList<Node> findPath()
  {  
    DijkstraNode current, start = map_[start_.x][start_.y] , 
        goal = map_[goal_.x][goal_.y];
    
    start.setGScore(0); // setting  the g score of the start to zero
         
    while(!open_list_.isEmpty())
    {
      current = lookingForBestNode(); // get node with lowest f Costs from open list   
      
      if (current.equals(goal)) // found goal
        return constructPath(current);
      
      open_list_.remove(current); // delete current node from open list
      
      LinkedList<DijkstraNode> adjacent_nodes = new LinkedList<DijkstraNode>();

      ArrayList<Node> edges = current.getNode().getEdges();
      if(edges != null)
      {
        for (int idx = 0, size = edges.size(); idx<size; ++idx)
        {
          DijkstraNode current_edje = map_[edges.get(idx).getPosition().x][edges.get(idx).getPosition().y];
          if(!current_edje.isObstacle())
          {
            adjacent_nodes.add(current_edje);
          }
        }
      }
           
      for (int idx = 0, size = adjacent_nodes.size(); idx<size; ++idx)
      {
    	DijkstraNode current_adj = adjacent_nodes.get(idx);
        // costs from current node are cheaper than previous costs
        if(current_adj.getGScore()>(current.getGScore() + COST)) 
        {
          current_adj.setParentNode(current); // set current node as previous for this node
          current_adj.setGScore(current.getGScore() + COST); // set g costs of this node (costs from start to this node)
        }
      }
    }
    
    return null;
  }
  
  
  private LinkedList<Node> constructPath (DijkstraNode goal)
  {
    LinkedList<Node> path = new LinkedList<Node>();
    while ( goal != null)
    {
      path.addFirst(goal.getNode());
      goal = goal.getParentNode();
    }
    
    return path;    
  }
  
  private DijkstraNode lookingForBestNode()
  {
    //TODO Βελτιστοποίηση αναζήτησης
    if (open_list_.isEmpty())
      return null;
    
    double lowest_cost = open_list_.get(0).getGScore();
    int lowest_idx=0;
    // Ξεκινάμε το for από το 1 μιας και έχουμε ήδη ορίσει ότι η καλύτερη τιμή
    // είναι ο πρώτος node
    for (int idx = 1, size = open_list_.size(); idx < size; ++idx)
    {
    	DijkstraNode current_node = open_list_.get(idx);
      if (current_node.getGScore() < lowest_cost )
      {
        lowest_cost = current_node.getGScore();
        lowest_idx = idx;
      }
    }
    
    return open_list_.get(lowest_idx);
  }
  
  
  // List containing nodes not visited but adjacent to visited nodes.
  private LinkedList<DijkstraNode> open_list_;

  // The variables for representing the goal point and the start point
  private Point goal_, start_;
    
  private DijkstraNode[][] map_;
   
  private final int COST = 1 ; 
  
  public class DijkstraNode
  {
    
    public DijkstraNode(Point position) 
    {
      this(position, false);
    }
    
    public DijkstraNode(Point position, boolean is_obstacle)
    {
      node_ = new Node(position);
      setParentNode(null);
      setGScore(Double.MAX_VALUE);
    }
    
    public DijkstraNode(Node node) 
    {
      node_=node;
      setParentNode(null);
      setGScore(Double.MAX_VALUE);
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
    protected void setAsObstacle(ArrayList<Node> edges)
    {
      node_.setAsObstacle(edges); 
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
    public DijkstraNode getParentNode()
    {
      return parent_node_;
    }

    /**
     * @param parent_node the parent_node_ to set
     */
    public void setParentNode(DijkstraNode parent_node)
    {
      parent_node_ = parent_node;
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
    
    public void setGScore(DijkstraNode previous_node,double g_score)
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
      DijkstraNode other = (DijkstraNode) obj;
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
      return "(" + getPosition().x + ", " + getPosition().y + "): g: " + getGScore();
    }
    
    // A private variable saving the informations regarding the node.
    private Node node_;
    
    // A private variable showing the parent node
    private DijkstraNode parent_node_;
     
    //Cost from start along best known path.
    private double g_score_;
  }

}
