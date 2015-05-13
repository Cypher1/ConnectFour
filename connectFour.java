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
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
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
    private static final int EASY = 0;
    private static final int MED = 1;
    private static final int HARD = 2;
    private static final int HUMAN = 3;
    private static final int START = 4;
    private static final int COL_BUTTON_START = 5;

    private JFrame f;
    private int gameMode;
    private Simulator simulator;
    //renderer

    //nested class that deals with button presses
    private class ConnectFourActionListener implements ActionListener {
    	private JFrame f;
    	private int button;
    	
    	public ConnectFourActionListener(JFrame f, int button){
    		this.f = f;
    		this.button = button;
    	}
    	
    	@Override
    	public void actionPerformed(ActionEvent e){
    		//check which button was pressed and call their respective function
            if((button >= EASY) && (button <= HARD)){
                //initialise game with appropriate difficulty
                connectFour.this.initGame(button, f); 
                System.out.println("DIFFICULTY " + button + " CHOSEN"); 
            }else if (button == HUMAN){
                //initialise a game against a human
                connectFour.this.initGame(button, f);
                System.out.println("HUMAN V HUMAN MODE CHOSEN");
            } else if ((button >= COL_BUTTON_START) && (button <= COL_BUTTON_START+7))
            {
                //MAKE A MOVE
                System.out.println("COLUMN " + (button-COL_BUTTON_START) + " CHOSEN");
                connectFour.this.moveMade(f, button-COL_BUTTON_START);
            } else if (button == START){
                //return to start screen
                connectFour.this.startScreen(f);
                System.out.println("RESTART PRESSED");
            }
    		
    	}
    }

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
		System.out.println("INIT GAME");
		
        //create renderer and startboard
        BasicBoardRenderer renderer = new BasicBoardRenderer();

	LinkedList<Player> players = new LinkedList<Player>();
	players.add(new EasyPlayer());
	players.add(new EasyPlayer());

	simulator = new Simulator(players);
	
        //Create grid bag constraint for layout of the JFrame
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = 0;
        c.weightx = 2;
        
	// Create the AI

        //remove all current buttons etc from frame for the new perspective
        f.getContentPane().removeAll();

        //create a button for each column
        for(int i = 0; i < simulator.getBoard().getWidth(); i++){
            //create button and set attributes
            JButton b_temp = new JButton("V");
            ConnectFourActionListener l_temp = new ConnectFourActionListener(f, COL_BUTTON_START+i);
            b_temp.addActionListener(l_temp);
            c.gridx = i;
            f.add(b_temp, c); 
        }

        //create and add a quit button
        JButton b_restart = new JButton("RESTART");
        ConnectFourActionListener l_restart = new ConnectFourActionListener(f, START);
        b_restart.addActionListener(l_restart);

        //create new grid bag layout for display
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = simulator.getBoard().getWidth();
        c.gridy = 2;
        c.weightx = 0.0;
        f.add(b_restart, c);

        //render the start board
		renderer.setBoard(simulator.getBoard());
		renderer.setFrame(f);
		//set up the input to make moves happen
        
		//make the window visible
		f.pack();
		f.setVisible(true);
		System.out.println("SHOWN");
	//start the game loop

	simulator.gameLoop();
	}

    public void startScreen(JFrame f){  
        //remove everything from f to give the new perspective
        f.getContentPane().removeAll();

        //create grid bag layout constraints to set the layout
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        //Create a board renderer and a new board
        BasicBoardRenderer renderer = new BasicBoardRenderer();
        BasicBoard initialBoard = new BasicBoard(NUM_PLAYERS, WINLEN); 

        JLabel label = new JLabel("  Choose a Play Mode:      ");
        //create the easy/medium/hard buttons
        JButton b_e = new JButton("EASY");
        JButton b_m = new JButton("MEDIUM");
        JButton b_h = new JButton("HARD");
        JButton b_2 = new JButton("2 PLAYER MODE");

        //add an action listener to the button (NB: need to get rid of magic numbers)
        b_e.addActionListener(new ConnectFourActionListener(f, EASY));
        b_m.addActionListener(new ConnectFourActionListener(f, MED));
        b_h.addActionListener(new ConnectFourActionListener(f, HARD));
        b_2.addActionListener(new ConnectFourActionListener(f, HUMAN));

        //add buttons to the window
        f.add(label, c);
        f.add(b_e, c);
        f.add(b_m, c);
        f.add(b_h, c);
        f.add(b_2, c);

        //initiate renderer attributes
        renderer.setBoard(initialBoard);
        renderer.setFrame(f);

        f.pack();
        f.setVisible(true);
    }

    public void moveMade(JFrame f, int column){

    }
}
