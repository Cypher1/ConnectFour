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
    public static final int QUIT_GAME = -3;
    public static final int UNDO = -2;

    //define the grid positions for buttons e.t.c.
    private static final int[] RESTART_BUTTON_PLACEMENT = {0, 7};
    private static final int RESTART_BUTTON_WIDTH = 2;
    private static final int[] QUIT_BUTTON_PLACEMENT = {0, 9};
    private static final int QUIT_BUTTON_WIDTH = 2;
    private static final int[] COL_BUTTON_PLACEMENT = {0, 1};
    private static final int[] COL_BUTTON_WEIGHTS = {2, 1};
    private static final int[] COL_BUTTON_PADDING = {10, 50*8};
    public static final int[] BOARD_PLACEMENT = {0, 1};
    public static final int BOARD_WIDTH = 7; //Need to update this for further use
    public static final int[] SIDEPANEL_PLACEMENT = {1, 1};
    public static final int[] MESSAGE_PLACEMENT = {0, 2};
    public static final int MESSAGE_WIDTH = 2;


    private JFrame f;
    private JLabel gameMessage;
    private int gameMode;
    private JButton startButton;
    private Simulator simulator;
    private LinkedList<Simulator> undorecord = new LinkedList<Simulator>();
    LinkedList<Player> players;

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
        f.setResizable(false);
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
        
        gameFrame();
        initBackend();
        simulator.gameUpdate();
    }

    /**
     *  Responsible for setting out and showing the buttons e.t.c. associated with the start screen
     */

    public void startScreen(JFrame f){  
        f.repaint();
        int width;
        int height;
        //remove everything from f to give the new perspective
        f.getContentPane().removeAll();
        width = 500;
        height = 600;
        f.getContentPane().setMinimumSize(new Dimension(width, height));

        GridBagConstraints c = new GridBagConstraints();
        JLabel title = new JLabel("<html>Connect Four</>");
        title.setFont (title.getFont ().deriveFont (24.0f));
        c.gridy = 8;
        f.add(title); 

        JLabel sub = new JLabel("Choose your game mode:");
        sub.setFont (sub.getFont ().deriveFont (22.0f));
        c.gridy = 12;
        c.gridx = 0;
        c.gridwidth = 2;
        f.add(sub,c);

        JButton b_e = new JButton("EASY");
        b_e.setPreferredSize(new Dimension (150,28));
        c.ipady = 5;
        c.gridy = 20;
        f.add(b_e, c);

        JButton b_m = new JButton("MEDIUM"); 
        b_m.setPreferredSize(new Dimension (150,28));
        c.gridy = 24;
        c.gridx = 0;
        f.add(b_m, c);

        JButton b_h = new JButton("HARD");
        b_h.setPreferredSize(new Dimension (150,28));
        c.gridy = 30;
        f.add(b_h, c);

        JButton b_2 = new JButton("2 PLAYER MODE");
        b_2.setPreferredSize(new Dimension (150,28));
        c.gridy = 34;
        f.add(b_2,c);
        
        JLabel selection = new JLabel("Select game size:");
        selection.setFont (selection.getFont ().deriveFont (16.0f));
        c.gridy = 38;
        f.add(selection,c);

        JLabel row = new JLabel("Row:");
        row.setFont (row.getFont ().deriveFont (14.0f));
        c.gridy = 42;
        c.gridwidth = 1;
        f.add(row,c);
        String[] items = {"4","6","7","8","9","10"};
      
        JComboBox<String> box = new JComboBox<>(items);
        box.setSelectedIndex(1);
        f.add(box);
        c.gridy = 46;
        f.add(box, c);

        JLabel col = new JLabel("Column:");
        col.setFont (col.getFont ().deriveFont (14.0f));
        c.gridy = 42;
        c.gridx = 1;
        f.add(col,c);
        
        JComboBox<String> box1 = new JComboBox<>(items);
        f.add(box1);
        box1.setSelectedIndex(2);
        c.gridy = 46;
        c.gridx = 1;
        f.add(box1, c);

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension (150,28));
        c.gridy = 50;
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
    
    /**
     *  responsible for initialising the buttons, labels and panels used during game runtime
     */
    private void gameFrame()
    {
        setColumnButtons();
        setSidePanel();

        f.pack();
        f.setVisible(true);
    }

    /**
     *  Responsible for creating players, renderer, simulator for the game as well as 
     *  starting the simulator thread that will run parallel to the GUI thread
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
        renderer.setGameMessage(gameMessage);
        renderer.render();

        //make the window visible
        f.pack();
        f.setVisible(true);
    }

    /**
     *  Responsible for making and setting out the buttons for each column of the connect four 
     *  board
     */
    public void setColumnButtons()
    {
        //create a board panel and correct GridBagConstraints
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridBagLayout());

        //get the height and width of the board for here!
        int width = 65 * 7; //!! change this to board 'rows'
        int height = 65 * 6; //!! change this to 'cols'
        boardPanel.setPreferredSize(new Dimension(width,height));

        //make the board panel invisible
        boardPanel.setOpaque(false);

        //grid bag layout for panel within frame
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = BOARD_PLACEMENT[1];
        c.gridx = BOARD_PLACEMENT[0];
        f.add(boardPanel, c);

        //Create grid bag layout columns buttons within panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = COL_BUTTON_PLACEMENT[0];
        c.weightx = COL_BUTTON_WEIGHTS[0];
        c.ipady = COL_BUTTON_PADDING[1];

        //create a button for each column
        for(int i = 0; i < BOARD_WIDTH; i++){
            //create button and set attributes
            JButton b_temp = new JButton("");
            ConnectFourActionListener l_temp = new ConnectFourActionListener(f, COL_BUTTON_START+i, this);
            b_temp.addActionListener(l_temp);
            b_temp.setOpaque(false);
            b_temp.setContentAreaFilled(false);
            b_temp.setBorderPainted(false);
            c.gridx = i;
            boardPanel.add(b_temp, c); 
        }
    }

    /**
     *  Responible for making and Laying out all attributes of the side panel present during game mode
     */
    public void setSidePanel()

    {
        // create a new JPanel and name set layout manager
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridBagLayout());

        //get the height and width of the board for here!
        int width = 65 * 7 / 2; //!! change this to board 'rows'
        int height = 65 * 6; //!! change this to 'cols'
        sidePanel.setPreferredSize(new Dimension(width,height));

        //grid bag layout for the side panel within the frame
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = SIDEPANEL_PLACEMENT[1];
        c.gridx = SIDEPANEL_PLACEMENT[0];
        f.add(sidePanel, c);

        //create and add a quit button
        JButton b_restart = new JButton("RESTART");
        ConnectFourActionListener l_restart = new ConnectFourActionListener(f, START, this);
        b_restart.addActionListener(l_restart);

        JButton b_undo = new JButton("UNDO");
        ConnectFourActionListener l_undo = new ConnectFourActionListener(f, UNDO, this);
        b_undo.addActionListener(l_undo);

        JButton b_quit = new JButton("QUIT GAME");
        ConnectFourActionListener l_quit = new ConnectFourActionListener(f, QUIT_GAME, this);
        b_quit.addActionListener(l_quit);
        
        //create new grid bag layout for the restart button and add to panel
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = RESTART_BUTTON_WIDTH;
        c.gridy = RESTART_BUTTON_PLACEMENT[1];
        
        //add all the buttons underneath each other
        sidePanel.add(b_restart, c);
        c.gridy = c.gridy+2;
        sidePanel.add(b_undo, c);
        c.gridy = c.gridy+2;
        sidePanel.add(b_quit, c);

        //create a game message object and add it to the panel
        gameMessage = new JLabel("");
        gameMessage.setFont(gameMessage.getFont().deriveFont(18.0f));
        c.gridy = MESSAGE_PLACEMENT[1];
        c.gridwidth = MESSAGE_WIDTH;
        sidePanel.add(gameMessage, c);
    }


    /**
     *  Deals with human game moves. It will check the current player is a human before invoking
     *  the makeMove function within the HumanPlayer class
     *  @param column will be a number from 0 to 6 indicating the column the human player wants to 
     *  drop a tile into
     */

    public void humanPlayerMove(int column)
    {
        //get the current player
        int player = simulator.getCurrentPlayer();
        Player currPlayer = players.get(player);

        //check the current player is human and invoke a move
        if (currPlayer instanceof HumanPlayer){
            undorecord.add(simulator.clone());
            HumanPlayer human = (HumanPlayer)currPlayer;
            human.makeMove(column);
        }
        simulator.gameUpdate();
    }

    /**
     *  Undoes a move by reseting the Simulator to the old version
     */
    public void undoSimulator(){
        if(undorecord.size() > 0){
            f.getContentPane().removeAll();
            simulator = undorecord.getLast().clone();
            undorecord.removeLast();
            BoardRenderer renderer = new BoardRenderer(); 
            //initiate renderer attributes
            renderer.setBoard(simulator.getBoard());
            renderer.setFrame(f);
            renderer.render();
            
            //set game frame and add game message label to the renderer 
            gameFrame();
            renderer.setGameMessage(gameMessage); 
            
            simulator.gameUpdate();
        }
    }
}
