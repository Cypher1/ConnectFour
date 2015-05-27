import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;

/**
* A class that deals with button presses of the buttons associated with the connectFour class
*/
public class ConnectFourActionListener implements ActionListener {
    private JFrame f;
    private int button;
    private connectFour game;        
    
    /**
    * A constructor for the ConnectFourActionListener. <p>
    * pre: the inputs are valid <b>
    * post: a new connectFourActionListener is created with the given parameters
    * @param f: the ui of the game
    * @param button: the id of the button which this action listener relates to 
    * @param game: the connectFour game which the button makes changes to
    */
    public ConnectFourActionListener(JFrame f, int button, connectFour game){
        this.f = f;
        this.button = button;
        this.game = game;
    }
    
    /**
    * The function which actually triggers the events that a button press should generate <p>
    * pre: the buttons have been set up, and the game is valid <br>
    * post: the action that the button was supposed to generate has been completed
    * @param e: the action event
    */
    public void actionPerformed(ActionEvent e){
        //check which button was pressed and call their respective function
        if((button >= game.EASY) && (button <= game.HARD)){
            //initialise game with appropriate difficulty
            game.setGameMode(button);
        } else if (button == game.HUMAN){
            //initialise a game against a human
            game.setGameMode(button);
        } else if (button == game.START){
            game.initGame(f);
        } else if (button == game.RESTART) {
            game.startScreen(f);
        } else if(button == game.UNDO){
            game.undoSimulator();
            //System.out.println("Undo pressed");
        } else if (button >= game.COL_BUTTON_START){
            game.humanPlayerMove(button - game.COL_BUTTON_START);
        } else if (button == game.QUIT_GAME){
            game.startScreen(f);
        }
    }
}

