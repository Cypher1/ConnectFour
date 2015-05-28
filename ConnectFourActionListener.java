import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JFrame;
import javax.swing.JComboBox;

/**
* A class that deals with button presses of the buttons associated with the connectFour class
*/
public class ConnectFourActionListener implements ActionListener {
    private JFrame f;
    private int button;
    private connectFour game;  
    private JComboBox comboBox = null;      
    
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
    * A constructor for the ConnectFourActionListener. <p>
    * pre: the inputs are valid <b>
    * post: a new connectFourActionListener is created with the given parameters
    * @param f: the ui of the game
    * @param button: the id of the button which this action listener relates to 
    * @param game: the connectFour game which the button makes changes to
    * @param comboBox: if the listener is listening for a comboBox, this will be a pointer to that combo box
    */
    public ConnectFourActionListener(JFrame f, int button, connectFour game, JComboBox comboBox)
    {
        this.f = f;
        this.button = button;
        this.game = game;
        this.comboBox = comboBox;
    }
    /**
    * The function which actually triggers the events that a button press should generate <p>
    * pre: the buttons have been set up, and the game is valid <br>
    * post: the action that the button was supposed to generate has been completed
    * @param e: the action event
    */
    public void actionPerformed(ActionEvent e){
        //check which button was pressed and call their respective function
        if((button >= game.EASY) && (button <= game.HUMAN)){
            //set the game mode
            game.setGameMode(button);

        } else if (button == game.START){
            //initialise the game
            game.initGame(f);

        } else if (button == game.RESTART) {
            //Go back to the start screen
            game.startScreen(f);

        } else if(button == game.UNDO){
            //undo the last move made
            game.undoSimulator();

        } else if (button >= game.COL_BUTTON_START){
            //register the move that was made
            game.humanPlayerMove(button - game.COL_BUTTON_START);

        } else if (button == game.QUIT_GAME){
            //return to the start screen
            game.startScreen(f);

        } else if (button == game.ROWS_IN){
            //register the height of the board that was chosen
            String input = (String)comboBox.getSelectedItem();
            int rows = Integer.parseInt(input);
            game.setBoardHeight(rows);

        }  else if (button == game.COLS_IN){
            //register the width of the board that was chosen
            String input = (String)comboBox.getSelectedItem();
            int cols = Integer.parseInt(input);
            game.setBoardWidth(cols);

        } else if (button == game.HINT){
            game.provideHint();
        }
    }
}

