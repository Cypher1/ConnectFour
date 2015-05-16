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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.imageio.ImageIO;
import java.util.LinkedList;
 
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

    private JFrame f;
    private int gameMode;
    private Simulator simulator;
    LinkedList<Player> players;

    //nested class that deals with button presses
   

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
		
		System.out.println("DONE");
    }
 
    public static void main(String[] args) {
        connectFour gameWindow = new connectFour();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(gameWindow);
    }
    

	public void initGame(int playType, JFrame f){
        System.out.println("INITIALISING GAME");
        //remove all current buttons etc from frame for the new perspective
        f.getContentPane().removeAll();

        this.gameMode = playType;

        //create players
        players = new LinkedList<Player>();
        players.add(new HumanPlayer());
        switch (gameMode)
        {
        case EASY:  players.add(new EasyPlayer());
            break;
        //case MED:   players.add(new MediumPlayer()); // my compiler couldn't find MediumPlayer class for some reason...
        //    break;
        case HARD:  players.add(new HardPlayer());
            break;
        case HUMAN: players.add(new HumanPlayer());
            break;
        }

        initBackend();
        gameFrame();
	}

    /**
        Responsible for setting out and showing the buttons e.t.c. associated with the start screen
    */
    public void startScreen(JFrame f){  
        //remove everything from f to give the new perspective
        f.getContentPane().removeAll();

        //create grid bag layout constraints to set the layout
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        JLabel label = new JLabel("  Choose a Play Mode:      ");
        //create the easy/medium/hard buttons
        JButton b_e = new JButton("EASY");
        JButton b_m = new JButton("MEDIUM");
        JButton b_h = new JButton("HARD");
        JButton b_2 = new JButton("2 PLAYER MODE");

        //add an action listener to the button (NB: need to get rid of magic numbers)
        b_e.addActionListener(new ConnectFourActionListener(f, EASY, this));
        b_m.addActionListener(new ConnectFourActionListener(f, MED, this));
        b_h.addActionListener(new ConnectFourActionListener(f, HARD, this));
        b_2.addActionListener(new ConnectFourActionListener(f, HUMAN, this));

        //add buttons to the window
        f.add(label, c);
        f.add(b_e, c);
        f.add(b_m, c);
        f.add(b_h, c);
        f.add(b_2, c);

        //Create a board renderer and a new board
        BasicBoardRenderer renderer = new BasicBoardRenderer();
        BasicBoard initialBoard = new BasicBoard(NUM_PLAYERS, WINLEN); 

        //initiate renderer attributes
        renderer.setBoard(initialBoard);
        renderer.setFrame(f);
        renderer.render();

        f.pack();
        f.setVisible(true);
    }

    /**
        responsible for initialising the buttons, labels and panels used during game runtime
    */
    private void gameFrame()
    {

        //Create grid bag constraint for layout of the JFrame
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.weightx = 2;

        //create a button for each column
        for(int i = 0; i < simulator.getBoard().getWidth(); i++){
            //create button and set attributes
            JButton b_temp = new JButton("V");
            ConnectFourActionListener l_temp = new ConnectFourActionListener(f, COL_BUTTON_START+i, this);
            b_temp.addActionListener(l_temp);
            c.gridx = i;
            f.add(b_temp, c); 
        }

        //create and add a quit button
        JButton b_restart = new JButton("RESTART");
        ConnectFourActionListener l_restart = new ConnectFourActionListener(f, START, this);
        b_restart.addActionListener(l_restart);

        //create new grid bag layout for quit button
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = simulator.getBoard().getWidth();
        c.gridy = 2;
        c.weightx = 0.0;
        f.add(b_restart, c);

        f.pack();
        f.setVisible(true);

        System.out.println("INITIALISED GAME FRAME");
    }

    /**
        Responsible for creating players, renderer, simulator for the game as well as 
        starting the simulator thread that will run parallel to the GUI thread
    */
    private void initBackend()
    {
        //initiate the simuator with players
        simulator = new Simulator(players);
        
        //render the start board
        //Create a board renderer and a new board
        BasicBoardRenderer renderer = new BasicBoardRenderer();
        BasicBoard initialBoard = new BasicBoard(NUM_PLAYERS, WINLEN); 

        //initiate renderer attributes
        renderer.setBoard(initialBoard);
        renderer.setFrame(f);
        renderer.render();

        //make the window visible
        f.pack();
        f.setVisible(true);
        System.out.println("SHOWN");

        //create a new thread for the simulator and start it
        simulator.getBoard().addRenderer(renderer);
        Thread simulatorThread = new Thread(simulator, "simulatorThread");
        simulatorThread.start();

        System.out.println("INITIALISED BACK END");
    }

    /**
        Deals with human game moves. It will check the current player is a human before invoking
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
    }
}
