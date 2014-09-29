package pathfinding;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Gui extends JFrame {
  
  private Map world_;
  private Point start = new Point(0,0), goal = new Point (4,4);
  
    public static void main(String[] args) {
        new Gui();
    }

    public Gui() 
    {
      EventQueue.invokeLater(new Runnable() 
      {
        @Override
        public void run() 
        {
          try 
          {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
          }
          catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) 
          {
          }
        
          final JFrame frame = new JFrame("Grid World Pathfinding");
          frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
          frame.setSize(1024, 768);
          frame.setLocationRelativeTo(null);
          frame.setVisible(true);
                
          Container container = frame.getContentPane();
          BorderLayout border_layout = new BorderLayout();
          container.setLayout(border_layout);
               
          
          world_ = new Map(5,5);
          world_.setObstacle(new Point(3,3), true);
          world_.setObstacle(new Point(2,2), true);
          
          final GridPanel panel = new GridPanel(world_);
          container.add(panel, BorderLayout.CENTER);
          panel.start_ = start;
          panel.goal_ = goal;
          
          JPanel buttonsPanel;
          buttonsPanel = new JPanel();
          FlowLayout f;
          f = new FlowLayout();
          buttonsPanel.setLayout(f);
          container.add(buttonsPanel, BorderLayout.SOUTH);

          final JButton add_walls_button;
          add_walls_button =  new JButton("Add walls");
          
          final JButton add_start_button;
          add_start_button =  new JButton("Add goal");
          
          final JButton add_goal_button;
          add_goal_button =  new JButton("Add goal");
          
          
          add_walls_button.setEnabled(false);
          add_walls_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              panel.add_wal_ = true ;
              panel.add_goal_ = false ;
              panel.add_start_ = false ;
              add_walls_button.setEnabled(false);
              add_start_button.setEnabled(true);
              add_goal_button.setEnabled(true);
            }
          });
          buttonsPanel.add(add_walls_button);
          
          
          //add_start_button.setEnabled(false);
          add_start_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              panel.add_wal_ = false ;
              panel.add_goal_ = false ;
              panel.add_start_ = true ;
              add_walls_button.setEnabled(true);
              add_start_button.setEnabled(false);
              add_goal_button.setEnabled(true);
            }
          });
          buttonsPanel.add(add_start_button);
          
          
          //add_goal_button.setEnabled(false);
          add_goal_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
              panel.add_wal_ = false ;
              panel.add_goal_ = true ;
              panel.add_start_ = false ;
              add_walls_button.setEnabled(true);
              add_start_button.setEnabled(true);
              add_goal_button.setEnabled(false);
            }
          });
          buttonsPanel.add(add_goal_button);
          
          JButton btnResetWorld = new JButton("Reset World");
          btnResetWorld.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) 
            {
              try
              {
                int w = Integer.valueOf(JOptionPane.showInputDialog("What is the world's width?"));
                int h = Integer.valueOf(JOptionPane.showInputDialog("What is the world's height?"));
                if (w > 0 && h >0 )
                {
                  world_ = new Map(h,w);
                  panel.resetWorld(world_);
                  panel.repaint();
                }
                else
                {
                  //TODO Να μπει μνμ προειδοποίησης
                }
              }
              catch(NumberFormatException e_num)
              {
                
              }
              
            }
          });
          buttonsPanel.add(btnResetWorld);
          
          JButton btnAstar = new JButton("ASTAR");
          btnAstar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
              
              AStar astar = new AStar(panel.start_,panel.goal_, world_);
              //System.out.println ("Starting A Star");
              //System.out.println("World:\n " + world_.toString());
              LinkedList<AStarNode> path = astar.findPath();
              if (path == null)
              {
                //System.out.println("empty");
                JOptionPane.showMessageDialog(frame,
                    "No path was found.",
                    "Message",
                    JOptionPane.INFORMATION_MESSAGE);
              }
              else
              {
                panel.drawPath(path);
                //System.out.println("Path: " + path.toString());
              }
                                   
            }
          });
          buttonsPanel.add(btnAstar);
        
        }
      });
    }
    
    //http://stackoverflow.com/questions/15421708/how-to-draw-grid-using-swing-class-java-and-detect-mouse-position-when-click-and
    public class GridPanel extends JPanel 
    {

        private int column_count_;
        private int row_count_;
        private List<Rectangle> cells_ = new ArrayList<Rectangle>();
        private Map world_;
        private Point selected_cell;
        private LinkedList<AStarNode> path_;
        
        
        // Variabels for setting the start point and the goal point
        public boolean add_wal_ = true;
        public boolean add_start_ = false;
        public boolean add_goal_ = false;
        
        public Point start_ , goal_ ;

        public GridPanel(Map world ) 
        {
          column_count_= world.getHeight();
          row_count_=world.getWidth();
          world_ = world;
          
          MouseAdapter mouseHandler;
          mouseHandler = new MouseAdapter() 
          {
            @Override
            public void mouseMoved(MouseEvent e) 
            {
              int width = getWidth();
              int height = getHeight();

              int cellWidth = width / column_count_;
              int cellHeight = height / row_count_;

              int column = e.getX() / cellWidth;
              int row = e.getY() / cellHeight;
              selected_cell = new Point(column, row);
              repaint();

            }
            
            @Override
            public void mouseClicked(MouseEvent e) 
            {
              int width = getWidth();
              int height = getHeight();

              int cellWidth = width / column_count_;
              int cellHeight = height / row_count_;

              int column = e.getX() / cellWidth;
              int row = e.getY() / cellHeight;

              selected_cell = new Point(column, row);
              if (add_wal_)
              {
                if (!world_.getNode(selected_cell).isObstacle())
                {
                  world_.setObstacle(selected_cell, true);
                }
                else
                {
                  world_.setObstacle(selected_cell, false);
                }
              }
              else if (add_start_)
              {
                start_ = selected_cell;
              }
              else if (add_goal_)
              {
                goal_ = selected_cell;
              }              
              
              repaint();

            }
          };
          addMouseMotionListener(mouseHandler);
          addMouseListener(mouseHandler);
        }
        
        
        public void resetWorld(Map world)
        {
          goal_ = null;
          start_ = null;
          column_count_= world.getHeight();
          row_count_=world.getWidth(); 
          world_ = world;
          resetWorld();
        }
        
        public void resetWorld()
        {
          try
          {
            cells_.clear();
            path_.clear();
          }
          catch(NullPointerException e)
          {
            
          }
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(200, 200);
        }

        @Override
        public void invalidate() {
            cells_.clear();
            selected_cell = null;
            super.invalidate();
        }
        
        protected void drawPath( LinkedList<AStarNode> path )
        {
          path_ = path;
          repaint();
        }

        @Override
        protected void paintComponent(Graphics g) 
        {
          super.paintComponent(g);
          Graphics2D g2d = (Graphics2D) g.create();

          int width = getWidth();
          int height = getHeight();

          int cellWidth = width / column_count_;
          int cellHeight = height / row_count_;

          int xOffset = (width - (column_count_ * cellWidth)) / 2;
          int yOffset = (height - (row_count_ * cellHeight)) / 2;

          if (cells_.isEmpty()) 
          {
            for (int row = 0; row < row_count_; row++) 
            {
              for (int col = 0; col < column_count_; col++) 
              {
                Rectangle cell = new Rectangle(
                              xOffset + (col * cellWidth),
                              yOffset + (row * cellHeight),
                              cellWidth,
                              cellHeight);
                cells_.add(cell);
                }
              }
            }
          
          for (int row = 0; row < row_count_; row++) 
          {
            for (int col = 0; col < column_count_; col++) 
            {
              Rectangle cell = cells_.get(row * column_count_ + col);
              if (world_.getNode(new Point(col,row)).isObstacle())
              {
                g2d.setColor(Color.GRAY);
                g2d.fill(cell);  
              }
              else
              {
                g2d.setColor(Color.WHITE);
                g2d.fill(cell);
              }
            }
          }

          if (selected_cell != null) 
          {
            try
            {
              int index = selected_cell.x + (selected_cell.y * column_count_);
              Rectangle cell = cells_.get(index);
              g2d.setColor(Color.BLUE);
              g2d.fill(cell);
            }
            catch(IndexOutOfBoundsException e)
            {
              //do nothing
            }
          }
    
          g2d.setColor(Color.GRAY);
          for (Rectangle cell : cells_) {
              g2d.draw(cell);
          }
          
          //draw start point
          if (start_ != null)
          {
            Rectangle cell = cells_.get((int) (start_.getY() * column_count_ + start_.getX()));
            g2d.setColor(Color.GREEN);
            g2d.fill(cell);  
          }
          //draw goal point
          
          if (goal_ != null)
          {
            Rectangle cell = cells_.get((int) (goal_.getY() * column_count_ + goal_.getX()));
            g2d.setColor(Color.RED);
            g2d.fill(cell);  
          }
          
          // draw path
          if (path_ != null)
          {
            g2d.setColor(Color.BLUE);
            for (int idx=0, size = path_.size(); idx < size; ++idx)
            {
              g2d.fillOval(
                  path_.get(idx).getPosition().x * cellWidth, 
                  path_.get(idx).getPosition().y * cellHeight, 
                  cellWidth, 
                  cellHeight);
            }
          }

          g2d.dispose();
        }
    }
}
