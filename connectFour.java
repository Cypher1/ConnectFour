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
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.ImageIcon;
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
        // Create the window
        JFrame f = new JFrame("Hello, !");
        // Sets the behavior for when the window is closed
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Add a layout manager so that the button is not placed on top of the label
        f.setLayout(new FlowLayout());
        // Add a label and a button
        f.add(new JLabel("Hello, world!"));
		try{
			BufferedImage buttonIcon = ImageIO.read(new File("buttonIconPath"));
			JButton b = new JButton(new ImageIcon(buttonIcon));
		
			f.add(b);
		}catch(IOException e){
			System.out.println("FILE MISSING");
		}
		
		//create a new button (NB: the above button creation wasn't working on my computer so next two lines won't be relevant when that works.)
		JButton b0 = new JButton("EASY");
		JButton b1 = new JButton("MEDIUM");
		//add button to the window
		f.add(b0);
		f.add(b1);
		
		//add an action listener to the button (NB: need to get rid of magic numbers)
		b0.addActionListener(new connectFourActionListener(f, 0));
		b1.addActionListener(new connectFourActionListener(f, 1));
		
		
        // Arrange the components inside the window
        f.pack();
        // By default, the window is not visible. Make it visible.
        f.setVisible(true);
        
        //NB: created board renderer e.t.c in a new class to make callable by the action listener class. 
        // not sure if this was the best way to do so...
        
    }
 
    public static void main(String[] args) {
        connectFour gameWindow = new connectFour();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(gameWindow);
    }
    

	public static void initGame(int playType){
		
		BoardRenderer renderer = new BasicBoardRenderer();
		Board startBoard = new BasicBoard(NUM_PLAYERS);
		
		// Create the board window
        JFrame f = new JFrame("Connect Four");
        // Sets the behavior for when the window is closed
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        //set the layout to a grid (Change this when graphics are used)
        f.setLayout(new GridLayout());
        
		renderer.setBoard(startBoard);
		renderer.setFrame(f);
		
		renderer.render();
		
		//make the window visible
		f.setVisible(true);
		
		//NB: create AI with difficulty type here? 
	}
 
}
