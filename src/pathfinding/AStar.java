/**
 * 
 */
package pathfinding;

import java.awt.Point; //for representing the points
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
    map_ = map;
  }
  
  public LinkedList<AStarNode> findPath()
  {
    //System.out.println("Creating open_list and close_list");
    open_list_ = new LinkedList<AStarNode>();
    closed_list_ = new LinkedList<AStarNode>();
    
    
    AStarNode current, start = new AStarNode(start_,false), goal = new AStarNode(goal_,false);
    start.setGScore(0); // setting  the g score of the start to zero
    
    start.setFScore(calculateDistance(start,goal));
    
    open_list_.add(start); // add starting node to open list
    //System.out.println("open list in the start: " + open_list_.toString() + "\n");
    
    while(!open_list_.isEmpty())
    {
      current = lookingForBestNode(); // get node with lowest f Costs from open list
      //System.out.println("current node: " + current.toString() + "\n");
      
      if (current.equals(goal)) // found goal
        return constructPath(current);
      
      open_list_.remove(current); // delete current node from open list
      closed_list_.add(current); // add current node to closed list
          
      LinkedList<AStarNode> adjacent_nodes= new LinkedList<AStarNode>();
       
      int current_x = current.getPosition().x;
      int current_y = current.getPosition().y;

      for (int left_top_y=current_y-1; left_top_y <current_y+2; ++left_top_y)
      {
        for (int left_top_x=current_x-1; left_top_x <current_x+2; ++left_top_x)
        {
          try
          {
            AStarNode current_adj = map_.getNode(new Point(left_top_x,left_top_y));
            
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
  
 
  private LinkedList<AStarNode> constructPath (AStarNode goal)
  {
    LinkedList<AStarNode> path = new LinkedList<AStarNode>();
    while (null != goal)
    {
      path.addFirst(goal);
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
  
  private Map map_;
  
  private final int COST = 1 ; 

}
