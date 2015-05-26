/*Main program for COMP2911-Project

    Start Date: 28th April 2015
    Written By: 
        Kirsten Hendriks
        Peng Herrork
        Lucy Kidd
        Michelle Phan
        Joshua Pratt
*/
import java.io.File;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.util.LinkedList;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.*;
import java.awt.event.*;

public class connectFour implements Runnable {

    private static final int NUM_PLAYERS = 2;
    private static final int WINLEN = 4;
    //define the buttons
    public static final int EASY = 0;
    public static final int MED = 1;
    public static final int HARD = 2;
    public static final int HUMAN = 3;
    public static final int START = 4;
    public static final int COL_BUTTON_START = 5;
    public static final int RESTART = -1;

    //define the grid positions for buttons e.t.c.
    private static final int[] RESTART_BUTTON_PLACEMENT = {0, 2};
    private static final int RESTART_BUTTON_WIDTH = 7;
    private static final int[] COL_BUTTON_PLACEMENT = {0, 1};
    private static final int[] COL_BUTTON_WEIGHTS = {2, 1};
    private static final int[] COL_BUTTON_PADDING = {0, 50*8};
    public static final int[] BOARD_PLACEMENT = {0, 1};
    public static final int BOARD_WIDTH = 7;
    private JButton startButton;

    private JFrame f;
    private JLabel gameMessage;
    private int gameMode;
    private Simulator simulator;
    LinkedList<Player> players;
    private Thread simulatorThread;
   

    @Override
    public void run() {
        System.out.println("RUNNING");
        // Create the window
        f = new JFrame("Connect Four!");
        System.out.println("RUNNING");
        // Add a layout manager so that the button is not placed on top of the label
        //reinitialise the JFrame for current use
        f.setLayout(new GridBagLayout());
        // Sets the behavior for when the window is closed
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 

        startScreen(f);
        
    }
 
    public static void main(String[] args) {
        connectFour gameWindow = new connectFour();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(gameWindow);
    }
    
    public void setGameMode(int inputgameMode) {
        startButton.setEnabled(true);
        gameMode = inputgameMode;
    }
    public void initGame(JFrame f){
        //remove all current buttons etc from frame for the new perspective
        f.getContentPane().removeAll();

        //create players
        players = new LinkedList<Player>();
        players.add(new HumanPlayer());
        switch (gameMode)
        {
        case EASY:  players.add(new EasyPlayer());
            break;
        case MED:   players.add(new MediumPlayer()); 
            break;
        case HARD:  players.add(new HardPlayer());
            break;
        case HUMAN: players.add(new HumanPlayer());
            break;
        }

        initBackend();
        gameFrame();
        simulator.gameUpdate();
    }


    /** Responsible for setting out and showing the buttons e.t.c. associated with the start screen
    */
    public void startScreen(JFrame f){  
        f.repaint();
        int width;
        int height;
        //remove everything from f to give the new perspective
        f.getContentPane().removeAll();
        width = 600;
        height = 700;
        f.getContentPane().setPreferredSize(new Dimension(width, height));

        GridBagConstraints c = new GridBagConstraints();
        JLabel title = new JLabel("<html>Connect Four</>");
        title.setFont (title.getFont ().deriveFont (24.0f));
        f.add(title); 

        JLabel sub = new JLabel("Choose your game mode:");
        sub.setFont (sub.getFont ().deriveFont (22.0f));
        c.gridy = 1;
        c.gridx = 0;
        c.gridwidth = 2;
        f.add(sub,c);

        JButton b_e = new JButton("EASY");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 4;
        c.gridy = 2;
        f.add(b_e, c);

        JButton b_m = new JButton("MEDIUM"); 
        c.gridy = 3;
        c.gridx = 0;
        f.add(b_m, c);

        JButton b_h = new JButton("HARD");
        c.gridy = 4;
        f.add(b_h, c);

        JButton b_2 = new JButton("2 PLAYER MODE");
        c.gridy = 5;
        f.add(b_2,c);
        
        JLabel selection = new JLabel("Select game size:");
        selection.setFont (selection.getFont ().deriveFont (16.0f));
        c.gridy = 6;
        f.add(selection,c);

        JLabel row = new JLabel("Row:");
        row.setFont (row.getFont ().deriveFont (14.0f));
        c.gridy = 7;
        c.gridwidth = 1;
        f.add(row,c);
        String[] items = {"4","6","7","8","9","10"};
      
        JComboBox<String> box = new JComboBox<>(items);
        f.add(box);
        c.gridy = 8;
        f.add(box, c);

        JLabel col = new JLabel("Column:");
        col.setFont (col.getFont ().deriveFont (14.0f));
        c.gridy = 7;
        c.gridx = 1;
        f.add(col,c);
        
        JComboBox<String> box1 = new JComboBox<>(items);
        f.add(box1);
        c.gridy = 8;
        c.gridx = 1;
        f.add(box1, c);

        startButton = new JButton("Start");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 9;
        c.gridx = 0;
        c.gridwidth = 2;
        f.add(startButton,c);
  
        b_e.addActionListener(new ConnectFourActionListener(f, EASY, this));
        b_m.addActionListener(new ConnectFourActionListener(f, MED, this));
        b_h.addActionListener(new ConnectFourActionListener(f, HARD, this));
        b_2.addActionListener(new ConnectFourActionListener(f, HUMAN, this));

        startButton.addActionListener(new ConnectFourActionListener(f, START, this));
        startButton.setEnabled(false);

        f.pack();
        f.setVisible(true); 
}

    /** responsible for initialising the buttons, labels and panels used during game runtime
    */
    private void gameFrame()
    {

        //Create grid bag constraint for layout of the JFrame
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = COL_BUTTON_PLACEMENT[1];
        c.weightx = COL_BUTTON_WEIGHTS[0];
        c.ipady = COL_BUTTON_PADDING[1];

        //create a button for each column
        for(int i = 0; i < simulator.getBoard().getWidth(); i++){
            //create button and set attributes
            JButton b_temp = new JButton("");
            ConnectFourActionListener l_temp = new ConnectFourActionListener(f, COL_BUTTON_START+i, this);
            b_temp.addActionListener(l_temp);
            b_temp.setOpaque(false);
            b_temp.setContentAreaFilled(false);
            b_temp.setBorderPainted(false);
            c.gridx = i;
            f.add(b_temp, c); 
        }

        //create and add a quit button
        JButton b_restart = new JButton("RESTART");
        ConnectFourActionListener l_restart = new ConnectFourActionListener(f, RESTART, this);
        b_restart.addActionListener(l_restart);

        //create new grid bag layout for quit button
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = simulator.getBoard().getWidth();
        c.gridy = RESTART_BUTTON_PLACEMENT[1];
        //c.weightx = RESTART_BUTTON_WIDTH;
        f.add(b_restart, c);

        f.pack();
        f.setVisible(true);
    }

    /**Responsible for creating players, renderer, simulator for the game as well as 
        starting the simulator thread that will run parallel to the GUI thread
    */
    private void initBackend()
    {
        //initiate the simuator with players
        int boardWidth = 7;
        int boardHeight = 6;
        simulator = new Simulator(players, boardWidth, boardHeight);
        
        //render the start board
        //Create a board renderer and a new board
        BoardRenderer renderer = new BoardRenderer(); 

        //initiate renderer attributes
        renderer.setBoard(simulator.getBoard());
        renderer.setFrame(f);
        renderer.render();

        //make the window visible
        f.pack();
        f.setVisible(true);
    }

    /** function responsible for updating play messages each turn
    */
    private void updateGameMessage(int player, int win)
    {
        String message = "Player " + player;
        this.gameMessage = new JLabel();
    }

    /**Deals with human game moves. It will check the current player is a human before invoking
        the makeMove function within the HumanPlayer class
        @param column will be a number from 0 to 6 indicating the column the human player wants to 
            drop a tile into
    */
    public void humanPlayerMove(int column)
    {
        //get the current player
        int player = simulator.getCurrentPlayer();
        Player currPlayer = players.get(player);

        //check the current player is human and invoke a move
        if (currPlayer instanceof HumanPlayer){
            HumanPlayer human = (HumanPlayer)currPlayer;
            human.makeMove(column);
        }
    simulator.gameUpdate();
    }
}
