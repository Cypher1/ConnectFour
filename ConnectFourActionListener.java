import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

/**A class that deals with button presses of the buttons associated with the connectFour class
*/

public class ConnectFourActionListener implements ActionListener {
    	private JFrame f;
    	private int button;
        private connectFour game;        
    	
    	public ConnectFourActionListener(JFrame f, int button, connectFour game){
    		this.f = f;
    		this.button = button;
            this.game = game;
    	}
    	
    	@Override
    	public void actionPerformed(ActionEvent e){
    		//check which button was pressed and call their respective function
       if((button >= game.EASY) && (button <= game.HARD)){
                //initialise game with appropriate difficulty
                game.setGameMode(button);
            } else if (button == game.HUMAN){
                //initialise a game against a human
                game.setGameMode(button);
            } else if ((button >= game.COL_BUTTON_START) && (button <= game.COL_BUTTON_START+7)){
                game.humanPlayerMove(button - game.COL_BUTTON_START);
            } else if (button == game.START){
              
                    game.initGame(f);
                
            } else if (button == game.RESTART) {
                game.startScreen(f);
            } else if(button == game.UNDO){
                game.undoSimulator();
                //System.out.println("Undo pressed");
            }
    	}
    }

