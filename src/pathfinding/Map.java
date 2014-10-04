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
public class Map
{
  
  public Map(int width, int height)
  {
    width_=width;
    height_=height;
    map_= new Node[width_][height_];
    initializeEmptyMap();    
  }
  
  /**
   * initializes all nodes. Their coordinates will be set correctly.
   */
  private void initializeEmptyMap()
  {
    // Creating the nodes
    for (int i=0; i<width_; ++i)
    {
      for (int j=0; j<height_; ++j)
      {
        map_[i][j] = new Node(new Point(i,j));        
      }
    }
    
    // Setting the adjacent cells
    
    for (int i=0; i<width_; ++i)
    {
      for (int j=0; j<height_; ++j)
      {
        getNode(i,j).setAsObstacle(getAjdacentNodes(new Point(i,j) ) ); 
      }
    }
  }
  
  /**
   * returns node at given coordinates.
   * <p>
   * Set the adjacent Nodes 
   *
   * @param point
   */
  private ArrayList<Node> getAjdacentNodes(Point point)
  {
    int current_x = point.x;
    int current_y = point.y;
    ArrayList<Node> adjacent_nodes = new ArrayList<Node>();
    
    for (int left_top_y=current_y-1; left_top_y <current_y+2; ++left_top_y)
    {
      for (int left_top_x=current_x-1; left_top_x <current_x+2; ++left_top_x)
      {
        try
        {
          Node current_adj = map_[left_top_x][left_top_y];

          if (current_adj.equals(getNode(current_x,current_y)))
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
    return adjacent_nodes;
  }
  
  
  /**
   * sets nodes obstacle field at given coordinates to given value.
   * <p>
   * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
   *
   * @param point
   * @param bool
   */
  public void setObstacle(Point point, boolean is_obstacle) 
  {
    if (is_obstacle)
    {
      map_[point.x][point.y].setAsObstacle();
    }
    else
    {
      map_[point.x][point.y].setAsObstacle(getAjdacentNodes(point));
    }
  }
  
  /**
   * returns node at given coordinates.
   * <p>
   * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
   *
   * @param x
   * @param y
   * @return node
   */
  public Node getNode(Point point) 
  {
    // TODO check parameter.
    return map_[point.x][point.y];
  }
  
  public Node getNode(int x, int y) 
  {
    // TODO check parameter.
    return map_[x][y];
  }
  
  public int getWidth()
  {
    return width_;
  }
  
  public int getHeight()
  {
    return height_;
  }
  
  /**
   * returns a String containing the map
   * 
   *
   * @return
   */
  @Override
  public String toString() {
    String string_map = "";
    
    for (int i=0; i<width_; i++)
      string_map+=" _";
    
    string_map+="\n";
    
    for(int j=height_-1; j >=0; --j)
    {
      string_map+="|";
      for (int i=0; i<width_; ++i)
      {
        if (map_[i][j].isObstacle() )
          string_map+=" #";
        else
          string_map+="  ";          
      }
      
      string_map+="|\n";
    }
       
    return string_map; 
  }
  
  // A private variable describing the map
  private Node[][] map_;
  
  // A private variable showing the with of the map;
  private int width_;
  
  // A private variable showing the height of the map;
  private int height_;

}
