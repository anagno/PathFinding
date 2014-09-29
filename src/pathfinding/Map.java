/**
 * 
 */
package pathfinding;

import java.awt.Point; //for representing the points

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
    map_= new AStarNode[width_][height_];
    initializeEmptyMap();    
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
  public AStarNode getNode(Point point) 
  {
    // TODO check parameter.
    return map_[point.x][point.y];
  }
  
  
  /**
   * sets nodes obstacle field at given coordinates to given value.
   * <p>
   * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
   *
   * @param point
   * @param bool
   */
  public void setObstacle(Point point, boolean bool) 
  {
    // TODO check parameter.
    map_[point.x][point.y].setObstacle(bool);
  }
  
  /**
   * initializes all nodes. Their coordinates will be set correctly.
   */
  private void initializeEmptyMap()
  {
    for (int i=0; i<width_; ++i)
    {
      for (int j=0; j<height_; ++j)
      {
        map_[i][j] = new AStarNode(new Point(i,j));
        
      }
    }
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
  
  public int getWidth()
  {
    return width_;
  }
  
  public int getHeight()
  {
    return height_;
  }
  
  // A private variable describing the map
  private AStarNode[][] map_;
  
  // A private variable showing the with of the map;
  private int width_;
  
  // A private variable showing the height of the map;
  private int height_;

}
