/**
 * 
 */
package pathfinding;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;


/**
 * @author anagno
 *
 */
public class PSO
{
  
  public PSO(Point start, Point goal, Map map, int population)
  {
    map_ = map;
    start_ = map_.getNode(start);
    goal_ = map_.getNode(goal);
    population_ = population; 
    particles_ = new Particle[population_];
    //TODO
  }
  
  public LinkedList<Node> findPath()
  {
    calculatePSO();
    LinkedList<Node> solution = new LinkedList<Node>();
    solution.addAll(global_best_.getPosition());
    return solution;
  }
  

  // Να βρω καλύτερο όνομα. Κατ' ουσία ειναι η κύρια μέθοδος
  public void calculatePSO()
  {

    initializePopulation();
    
    global_best_ = particles_[0];
    
    for (int idx=1; idx<population_; ++idx)
    {
      //if (particles_[i].position<pbest)
      //  particles_[i].pbest = current position

      if (particles_[idx].getFitness() < global_best_.getFitness() )
      {
        global_best_ = particles_[idx];
      }
      
      // compute velocity
      // u_p(t) = u_p(t-1) +c1*rand_1(pbest(t-1) - x(t-1)) +c2*rand_2(gbest(t-1) - x(t-2))
      //w=inertia factor
      // update position
      // x(t) = x(t-1) + u_p(t)
    }
  }

  // Function that initializes the population
  public void initializePopulation()
  { 
    for (int idx = 0; idx <population_; )
    {
      ArrayList<Node> possible_solution = new ArrayList<Node>(); 
      ArrayList<Node> used_nodes = new ArrayList<Node>();
      
      possible_solution.add(start_);
      used_nodes.add(start_);
           
      BEGIN_OF_SOLUTION:
      while(true)
      {
        Node current_node = possible_solution.get(possible_solution.size() - 1), next_node;
               
        // Άμα δεν υπάρχουν ακμες αφαιρούμε το κόμβο και τον προσθέτουμε στους χρησιμοποιημένους και πάμε
        // ένα βήμα πίσω. 
        // Θεωρητικά δεν πρέπει να χρησιμοποιηθεί ο κώδικας μιας και ελέγχουμε αν ειναι
        // εμπόδιο στον κώδικα (μόνο αν είναι εμπόδιο ο κόμβος δεν έχει ακμές) -- ΔΕΝ ΙΣΧΥΕΙ !!! 
        // Αφαίρεσα τον κώδικα που ελέγχει για εμπόδια διότι έδεινε χειρότερες λύσεις ...
        // ΔΕΝ ΕΧΩ ΙΔΕΑ ΓΙΑ ΠΟΙΟ ΛΟΓΟ !!!
        if (current_node.getEdges() == null)
        {
          used_nodes.add(current_node);
          possible_solution.remove(possible_solution.size() - 1);
          break BEGIN_OF_SOLUTION;
        }
        
        //Γιατί άμα την αφαιρέσω απ` ευθείας, επειδή είναι δείκτης φεύγει για πάντα !!!
        @SuppressWarnings("unchecked")
        ArrayList<Node> edges = (ArrayList<Node>) current_node.getEdges().clone();
        
        // Διαλέγουμε τον επόμενο κόμβο εδώ
        while(edges.size()>=0)
        {
          // Έχουμε χρησιμοποιήσει όλες τις ενναλακτικές και δεν μπορούμε να πάμε κάπου αλλου άρα πάμε πίσω.
          if (edges.isEmpty() )
          {
            possible_solution.remove(possible_solution.size() - 1); 
            break;
          }
          
          // Διαλέγουμε έναν κόμβο στην τύχη
          int rand_number = randInt(0, edges.size()-1);
          next_node = edges.remove(rand_number);    
          
          // next_node.isObstacle() ||  . Εναλακτικά θα μπορούσαμε να βάλουμε και αυτό μέσα αλλά για κάποιο λόγο
          // χωρίς αυτό η λύση είναι καλύτερη. 
          // Άμα διαλέξουμε κάποιο κόμβο που έχουμε ήδη χρησιμοποιήσει προχωράμε
          if( used_nodes.contains(next_node))
          {
            continue;
          }       

          //Τον τοποθετούμε στους χρησιμοποιημένους για να μην τον ξαναχρησιμοποιήσουμε
          used_nodes.add(next_node);
          
          // Άμα ο επόμενος κόμβος δεν περιλαμβάνεται στην λύση τον προσθέτουμε και συνεχίζουμε
          if (!possible_solution.contains(next_node))
          {
            
            possible_solution.add(next_node);
            
            // Άμα είναι ίσος με τον τελικό κόμβο τότε βρήκαμε την λύση
            if(next_node.equals(goal_))
            {
              break BEGIN_OF_SOLUTION;
            }
            
            // Υπάρχουν κύκλοι στην λύση άρα δεν μας κάνει. Κανονικά δεν πρέπει να συμβεί !!!
            if(possible_solution.size()>= ( (map_.getHeight()*map_.getWidth()) -1) )
            {
              break BEGIN_OF_SOLUTION;
            }
          }
          
          break;     
        }

      }                   
      // Άμα έχουμε ως τελευταίο κόμβο την λύση τότε την προσθέτουμε την λύση στα σωματίδια.
      if (possible_solution.get(possible_solution.size() - 1) == goal_)
      {
        particles_[idx] = new Particle(possible_solution);
        ++idx;
        used_nodes.clear();
        
      }
      
    } 
  } 
  
  
  //http://stackoverflow.com/questions/363681/generating-random-integers-in-a-range-with-java
  public static int randInt(int min, int max) 
  {
    // NOTE: Usually this should be a field rather than a method
    // variable so that it is not re-seeded every call.
    Random rand = new Random();

    // nextInt is normally exclusive of the top value,
    // so add 1 to make it inclusive
    int random_num = rand.nextInt((max - min) + 1) + min;

    return random_num;
    }

  
  // The variables for representing the goal point and the start point
  private Node goal_, start_;

  // The population of the PSO
  private int population_;
  
  // The actual populations of particles
  private Particle[] particles_;
  
  // The current gbest
  private Particle global_best_;
  
  // The map that is used
  private final Map map_;
  
  private class Particle
  {
    
    public Particle(ArrayList<Node> solution)
    {
      position_ = new Position(solution);
      personal_best_ = position_;
      fitness_ = calculateFitness(position_);  
    }
    
    public ArrayList<Node> getPosition()
    {
      return position_.getPosition();
    }
      
    public double getFitness()
    {
      return fitness_;
    }
    
    public void updatePosition(ArrayList<Node> position)
    {
      position_.update(position);
      fitness_ = calculateFitness (position_);
      if( calculateFitness(personal_best_) > fitness_)
      {
        personal_best_ = position_;
      }
    }
    
    private double calculateFitness(Position position)
    {
      return (double) position.getPosition().size();
    }
      
    private Position position_;
    //private    velocity_;
    
    // The current pbest
    private Position personal_best_;
    
    private double fitness_;
    
    
    private class Position
    {
      public Position(ArrayList<Node> position)
      {
        solution_ = position;
      }
      
      public ArrayList<Node> getPosition()
      {
        return solution_;
      }
      
      public void update(ArrayList<Node> new_solution)
      {
        solution_ = new_solution;
      }
      
      private ArrayList<Node> solution_;
    }
    

    
    //private class Velocity
    //{
      // Θα πρέπει να μπουν μάλλον δύο είδη κινήσεων. 
      // Το ένα θα είναι ανεξάρτητο και θα λαμβένει υπόψιν μόνο την τωρινή
      // θέση του σωματιδίου ενώ το άλλο θα λαμβάνει υπόψιν 
      // και το pbset και το gbest
      
      

    //  private Node first_node;

    //  private Node second_node;
      
    //}
    
  }
}
