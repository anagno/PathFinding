/**
 * 
 */
package pathfinding;

import java.awt.Point;
import java.util.ArrayList;



/**
 * @author anagno
 *
 */
public class PSO
{
  
  public PSO(Point start, Point goal, Map map)
  {
    start_ = start;
    goal_ = goal;
    //TODO
  }
  

  
  // The variables for representing the goal point and the start point
  private Point goal_, start_;

  // The population of the PSO
  private int population_;
  
  // The actual populations of particles
  private Particle particles_;
  
  // The current gbest
  private Particle global_best;
  
  private class Particle
  {
    public Particle(Position solution)
    {
      position_ = solution;
    }
    
    private double calculateFitness()
    {
      double fitness=0;
      
      
      return fitness;
    }
    
    /**
     * @return the fitness_
     */
    public double getFitness()
    {
      return fitness_;
    }

    /**
     * @param fitness_ the fitness_ to set
     */
    public void setFitness()
    {
      calculateFitness();
    }
    
    /**
     * @return the position_
     */
    public Position getPosition()
    {
      return position_;
    }

    /**
     * @param position_ the position_ to set
     */
    public void setPosition(Position position)
    {
      position_ = position;
    }
    
    private void addVelcocity(Particle particle, Velocity velocity)
    {
      
    }
    
    private void addVelcocity(Velocity particle, Velocity velocity)
    {
      
    }
    
    private Position position_;
    //private    velocity_;
    
    // The current pbest
    private Particle personal_best_;
    // The current gbest
    private Particle global_best_;
    
    private double fitness_;
    
    
    private class Position
    {
    
      /**
       * @return the position_
       */
      public ArrayList<Node> getPosition()
      {
        return position_;
      }

      /**
       * @param position_ the position_ to set
       */
      public void setPosition(ArrayList<Node> position)
      {
        position_ = position;
      }
      
      private ArrayList<Node> position_;
      
    }
    
    private class Velocity
    {
      
    }
    
  }
}