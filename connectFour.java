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
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
 
public class connectFour implements Runnable {

    private static final int NUM_PLAYERS = 2;

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

        f.add(new JButton("Press me!"));
        // Arrange the components inside the window
        f.pack();
        // By default, the window is not visible. Make it visible.
        f.setVisible(true);

	//work out how to use events but for now just render the board once
	BoardRenderer renderer = new BasicBoardRenderer();

	Board startBoard = new BasicBoard(NUM_PLAYERS);

	renderer.setBoard(startBoard);
	renderer.setFrame(f);
	
	renderer.render();
    }
 
    public static void main(String[] args) {
        connectFour gameWindow = new connectFour();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(gameWindow);
    }
 
}
