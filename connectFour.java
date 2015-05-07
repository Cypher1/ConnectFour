/*Main program for COMP2911-Project

	Start Date: 28th April 2015
	Written By: 
		Kirsten Hendriks
		Peng Herrork
		Lucy Kidd
		Michelle Phan
		Joshua Pratt
*/
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
 
public class connectFour implements Runnable {

    private static final int NUM_PLAYERS = 2;
    
    //nested class that deals with button presses
    private class connectFourActionListener implements ActionListener {
    	private JFrame f;
    	private int difficulty;
    	
    	public connectFourActionListener(JFrame f, int difficulty){
    		this.f = f;
    		this.difficulty = difficulty;
    	}
    	
    	@Override
    	public void actionPerformed(ActionEvent e){
    		//log action
    		System.out.println("BUTTON " + this.difficulty + " PRESSED" );
    		//destroy current window
    		this.f.dispose();
    		//create board renderer, players and board
    		connectFour.initGame(difficulty);
    	}
    }

    @Override
    public void run() {
    	System.out.println("RUNNING");
        // Create the window
    	JFrame f = new JFrame("Connect Four!");
    	System.out.println("RUNNING");
        // Add a layout manager so that the button is not placed on top of the label
        f.setLayout(new FlowLayout());
        // Add a label and a button
        f.add(new JLabel("Connect Four!"));
	

    	//create a new button (NB: the above button creation wasn't working on my computer so next two lines won't be relevant when that works.)
		JButton b0 = new JButton("EASY");
		JButton b1 = new JButton("MEDIUM");
		JButton b2 = new JButton("HARD");
		//add button to the window
		f.add(b0);
		f.add(b1);
		f.add(b2);


		//add an action listener to the button (NB: need to get rid of magic numbers)
		b0.addActionListener(new connectFourActionListener(f, 0));
		b1.addActionListener(new connectFourActionListener(f, 1));
		b2.addActionListener(new connectFourActionListener(f, 2));
		
		f.pack();
		f.setVisible(true);
		
		System.out.println("DONE");
    }
 
    public static void main(String[] args) {
        connectFour gameWindow = new connectFour();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(gameWindow);
    }
    

	public static void initGame(int playType){
		System.out.println("INIT GAME");
		BasicBoardRenderer renderer = new BasicBoardRenderer();
		Board startBoard = new BasicBoard(NUM_PLAYERS);

		// Create the AI
							
		// Create the board window
        JFrame f = new JFrame("Connect Four");
    	// Sets the behavior for when the window is closed
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		//set the layout to a grid (Change this when graphics are used)
		f.setLayout(new GridLayout());
        	
		renderer.setBoard(startBoard);
		renderer.setFrame(f);
		//renderer.render();
		//set up the input to make moves happen
				
		//make the window visible
		f.pack();
		f.setVisible(true);
		System.out.println("SHOWN");
	}
 
}
