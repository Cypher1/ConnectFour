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
    private static final int x = 0;
    private static final int y = 1;
    
    //define the buttons
    public static final int EASY = 0;
    public static final int MED = 1;
    public static final int HARD = 2;
    public static final int HUMAN = 3;
    public static final int START = 4;
    public static final int COL_BUTTON_START = 5;
    public static final int RESTART = -1;
    public static final int UNDO = -2;
    public static final int QUIT_GAME = -3;
    public static final int ROWS_IN = -4;
    public static final int COLS_IN = -5;
    public static final int HINT = -6;
    //define the grid positions for buttons e.t.c.
    private static final int[] RESTART_BUTTON_PLACEMENT = {0, 7};
    private static final int RESTART_BUTTON_WIDTH = 2;
    private static final int[] QUIT_BUTTON_PLACEMENT = {0, 9};
    private static final int QUIT_BUTTON_WIDTH = 2;
    private static final int[] COL_BUTTON_PLACEMENT = {0, 1};
    private static final int[] COL_BUTTON_WEIGHTS = {2, 1};
    private static final int[] COL_BUTTON_PADDING = {10, 50*8};
    public static final int[] BOARD_PLACEMENT = {0, 1};
    public static final int[] SIDEPANEL_PLACEMENT = {1, 1};
    public static final int[] SIDEPANEL_SIZE = {65 * 7 / 2, 65 * 4};
    public static final int[] MESSAGE_PLACEMENT = {0, 2};
    public static final int MESSAGE_WIDTH = 2;
    public static final int[] BUTTON_SIZE = {150, 28};

    //these are from board renderer sizes and spacing
    public static final int PIXELS_PER_COL = 60 + 5; 
    public static final int PIXELS_PER_ROW = 60 + 5; 


    private JFrame f;
    private JLabel gameMessage;
    private int gameMode;
    
    private JButton startButton;
    private JButton setEasyButton;
    private JButton setMediumButton;
    private JButton setHardButton;
    private JButton setTwoPlayerButton;

    private Simulator simulator;
    private LinkedList<Simulator> undorecord = new LinkedList<Simulator>();
    LinkedList<Player> players;
    private int[] boardSize = {7, 6};

    @Override
    public void run() {
        System.out.println("RUNNING");
        // Create the window
        f = new JFrame("Connect Four!");
        // Add a layout manager so that the button is not placed on top of the label
        //reinitialise the JFrame for current use
        f.setLayout(new GridBagLayout());
        // Sets the behavior for when the window is closed
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        f.setResizable(false);
        startScreen(f);
    }
 
    /**
    * Sets up a new connectFour object and sets up a game window
    */
    public static void main(String[] args) {
        connectFour gameWindow = new connectFour();
        // Schedules the application to be run at the correct time in the event queue.
        SwingUtilities.invokeLater(gameWindow);
    }
    
    /**
    * A setter function for the game modes of the connectFour object
    * pre: the given game mode is a valid representation of one of the four game modes<br>
    * post: the game mode has now been set to the given one
    * @param inputgameMode: the int representation of the four different game modes:
    * easy, medium, hard and human vs human
    */
    public void setGameMode(int inputgameMode) {
        startButton.setEnabled(true);
        gameMode = inputgameMode;

        //highlight the chosen button?
        Color baseColor = new JButton().getBackground();
        Color highlight = Color.white;
    
        setEasyButton.setBackground(baseColor);
        setMediumButton.setBackground(baseColor);
        setHardButton.setBackground(baseColor);
        setTwoPlayerButton.setBackground(baseColor);

        switch(inputgameMode){
            case EASY: setEasyButton.setBackground(highlight); break;
            case MED: setMediumButton.setBackground(highlight); break;
            case HARD: setHardButton.setBackground(highlight); break;
            case HUMAN: setTwoPlayerButton.setBackground(highlight); break;
        }
    }

    /**
     * A function that sets the width of the board, should be called by the action listener 
     * when the number of columns is changed <p>
     * pre: the width given is in between the valid acceptable width<br>
     * post: the board width has been set to the given width
     * @param width: the desired number of columns on the board
     */
    public void setBoardWidth(int width){
        this.boardSize[x] = width;
    }

    /**
     * A function that sets the height of the board, should be called by the action listener 
     * when the number of rows is changed<p>
     * pre: the height given is in between the valid acceptable heights<br>
     * post: the board height has been set to the given height
     * @param height: the desired number of rows on the board
     */
    public void setBoardHeight(int height){
        this.boardSize[y] = height;
    }
    
    /**
    * Responsible for the initialisation of the game by creating players, updating the user
    * interface for gameplay and creating all backend objects needed for game simulation <p>
    * pre: the given JFrame is valid <br>
    * post: the game has been initialised with the given JFrame, the players have been set
    * @param f: is the frame the game will be displayed in
    */
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
        
        undorecord = new LinkedList<Simulator>();
        
        gameFrame();
        initBackend();
        simulator.gameUpdate();
    }

    /**
     *  Responsible for constructing, setting out and showing all objects
     *  in the start screen <p>
     *  pre: the given JFrame is valid <br>
     *  post: the screen has been set to the starting screen
     *  @param f: the frame that the start screen will be shown in
     */

    public void startScreen(JFrame f){  
        f.repaint();
        int width;
        int height;
        //remove everything from f to give the new perspective
        f.getContentPane().removeAll();
        width = 500;
        height = 500;
        f.setMinimumSize(new Dimension(width, height));

        GridBagConstraints c = new GridBagConstraints();
        JLabel title = new JLabel("Connect Four");
        title.setFont (title.getFont ().deriveFont(24.0f));
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = 0; //insert at the top of the screen
        c.gridwidth = 2;
        c.insets = new Insets(5,0,0,0);  //top padding
        f.add(title, c); 

        JLabel sub = new JLabel("Choose your game mode:");
        sub.setFont (sub.getFont ().deriveFont (22.0f));
        c.gridy = c.gridy+1;
        f.add(sub,c);

        setEasyButton = new JButton("EASY");
        setEasyButton.setPreferredSize(new Dimension (BUTTON_SIZE[x], BUTTON_SIZE[y]));
        c.gridy = c.gridy+1;
        f.add(setEasyButton, c);

        setMediumButton = new JButton("MEDIUM"); 
        setMediumButton.setPreferredSize(new Dimension (BUTTON_SIZE[x],BUTTON_SIZE[y]));
        c.gridy = c.gridy+1;
        f.add(setMediumButton, c);

        setHardButton = new JButton("HARD");
        setHardButton.setPreferredSize(new Dimension (BUTTON_SIZE[x], BUTTON_SIZE[y]));
        c.gridy = c.gridy+1;
        f.add(setHardButton, c);

        setTwoPlayerButton = new JButton("2 PLAYER MODE");
        setTwoPlayerButton.setPreferredSize(new Dimension (BUTTON_SIZE[x], BUTTON_SIZE[y]));
        c.gridy = c.gridy+1;
        f.add(setTwoPlayerButton,c);
        
        JLabel selection = new JLabel("Select game size:");
        selection.setFont (selection.getFont ().deriveFont (16.0f));
        c.gridwidth = 2;
        c.gridy = c.gridy+1;
        f.add(selection,c);

        JLabel row = new JLabel("Row:   ");
        row.setFont (row.getFont ().deriveFont (14.0f));
        c.anchor = GridBagConstraints.EAST;
        c.insets = new Insets(5,10,0,10);
        c.gridwidth = 1;
        c.gridy = c.gridy+1;
        f.add(row,c);

        JLabel col = new JLabel("Column:");
        col.setFont (col.getFont ().deriveFont (14.0f));
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 1;
        f.add(col,c);
        
        String[] items = {"4","5","6","7","8","9","10"};
        JComboBox<String> box_cols = new JComboBox<>(items);
        box_cols.setSelectedIndex(boardSize[0]-4);
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(5,15,0,10);
        c.gridy = c.gridy+1;
        c.gridx = 1;
        c.weightx = width/2;
        f.add(box_cols, c);

        JComboBox<String> box_rows = new JComboBox<>(items);
        box_rows.setSelectedIndex(boardSize[1]-4); 

        c.anchor = GridBagConstraints.EAST;
        c.gridx = 0;
        f.add(box_rows, c);

        startButton = new JButton("Start");
        startButton.setPreferredSize(new Dimension (BUTTON_SIZE[x], BUTTON_SIZE[y]));
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = c.gridy + 1;
        c.gridwidth = 2;
        f.add(startButton,c);
  
        setEasyButton.addActionListener(new ConnectFourActionListener(f, EASY, this));
        setMediumButton.addActionListener(new ConnectFourActionListener(f, MED, this));
        setHardButton.addActionListener(new ConnectFourActionListener(f, HARD, this));
        setTwoPlayerButton.addActionListener(new ConnectFourActionListener(f, HUMAN, this));
        box_rows.addActionListener(new ConnectFourActionListener(f, ROWS_IN, this, box_rows));
        box_cols.addActionListener(new ConnectFourActionListener(f, COLS_IN, this, box_cols));
        startButton.addActionListener(new ConnectFourActionListener(f, START, this));
        startButton.setEnabled(false);

        f.pack();
        f.setVisible(true); 
    }
    
    /**
     * Responsible for the initialisation of all User Interface objects
     * used during the game <p>
     * pre: the game frame so far has not been corrupted<br>
     * post: the game frame includes all the buttons and side pale
     */
    private void gameFrame(){
        f.getContentPane().removeAll();
        f.setMinimumSize(new Dimension(-1, -1));

        setColumnButtons();
        setSidePanel();

        f.pack();
        f.repaint();
        f.setVisible(true);
    }

    /**
     *  Responsible for creating players, renderer, simulator for the game as well as 
     *  starting the simulator thread that will run parallel to the GUI thread <p>
     *  pre: the frame has not been corrupted so far<br>
     *  post: a board renderer and simulator has been created and included
     */
    private void initBackend()
    {
        //initiate the simuator with players;
        simulator = new Simulator(players, boardSize[0], boardSize[1]);
        //clear all previuos simulators saved for the undo function
        LinkedList<Simulator> undorecord = new LinkedList<Simulator>();
        
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
     *  board <p>
     *  pre: the frame has not been corrupted so far <br>
     *  post: the column buttons have been set in the frame
     */
    public void setColumnButtons()
    {
        //create a board panel and correct GridBagConstraints
        JPanel boardPanel = new JPanel();
        boardPanel.setLayout(new GridBagLayout());

        //get the height and width of the board
        int width = boardSize[x] * PIXELS_PER_COL;
        int height = boardSize[y] * PIXELS_PER_ROW; 

        boardPanel.setPreferredSize(new Dimension(width,height));

        //make the board panel invisible
        boardPanel.setOpaque(false);

        //grid bag layout for panel within frame
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = BOARD_PLACEMENT[y];
        c.gridx = BOARD_PLACEMENT[x];
        f.add(boardPanel, c);

        //Create grid bag layout columns buttons within panel
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridy = COL_BUTTON_PLACEMENT[y];
        c.weightx = COL_BUTTON_WEIGHTS[x];
        c.gridheight = GridBagConstraints.REMAINDER;
        c.ipady = height;

        //create a button for each column
        for(int i = 0; i < boardSize[x]; i++){
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
     *  Responible for making and Laying out all attributes of the side panel present 
     *  during game mode <p>
     *  pre: the frame has not been corrupted so far<br>
     *  post: the side panel has been populated with the appropriate buttons
     */
    public void setSidePanel()

    {
        // create a new JPanel and name set layout manager
        JPanel sidePanel = new JPanel();
        sidePanel.setLayout(new GridBagLayout());

        //get the height and width of the board for here!
        sidePanel.setPreferredSize(new Dimension(SIDEPANEL_SIZE[x],SIDEPANEL_SIZE[y]));

        //grid bag layout for the side panel within the frame
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = SIDEPANEL_PLACEMENT[y];
        c.gridx = SIDEPANEL_PLACEMENT[x];
        f.add(sidePanel, c);

        //create and add a quit button
        JButton b_restart = new JButton("RESTART");
        ConnectFourActionListener l_restart = new ConnectFourActionListener(f, START, this);
        b_restart.setPreferredSize(new Dimension (BUTTON_SIZE[x], BUTTON_SIZE[y]));
        b_restart.addActionListener(l_restart);

        JButton b_undo = new JButton("UNDO");
        ConnectFourActionListener l_undo = new ConnectFourActionListener(f, UNDO, this);
        b_undo.setPreferredSize(new Dimension (BUTTON_SIZE[x], BUTTON_SIZE[y]));
        b_undo.addActionListener(l_undo);

        JButton b_quit = new JButton("QUIT GAME");
        ConnectFourActionListener l_quit = new ConnectFourActionListener(f, QUIT_GAME, this);
        b_quit.setPreferredSize(new Dimension (BUTTON_SIZE[x], BUTTON_SIZE[y]));
        b_quit.addActionListener(l_quit);

        JButton b_hint = new JButton("HINT");
        ConnectFourActionListener l_hint = new ConnectFourActionListener(f, HINT, this);
        b_hint.setPreferredSize(new Dimension (BUTTON_SIZE[x], BUTTON_SIZE[y]));
        b_hint.addActionListener(l_hint);        
        
        //create new grid bag layout for the restart button and add to panel
        c = new GridBagConstraints();
        c.gridwidth = RESTART_BUTTON_WIDTH;
        c.gridy = RESTART_BUTTON_PLACEMENT[y];
        c.insets = new Insets(10,0,0,0);  //internal padding for buttons
        
        //add all the buttons underneath each other
        sidePanel.add(b_restart, c);
        c.gridy = c.gridy+1;
        sidePanel.add(b_undo, c);
        c.gridy = c.gridy+1;
        sidePanel.add(b_quit, c);
        c.gridy = c.gridy+1;
        sidePanel.add(b_hint, c);

        //create a game message object and add it to the panel
        gameMessage = new JLabel("");
        gameMessage.setFont(gameMessage.getFont().deriveFont(18.0f));
        c.anchor = GridBagConstraints.CENTER;
        c.gridy = MESSAGE_PLACEMENT[y];
        c.gridwidth = MESSAGE_WIDTH;
        sidePanel.add(gameMessage, c);
    }


    /**
     *  Deals with human game moves. It will check the current player is a human before invoking
     *  the makeMove function within the HumanPlayer class <p>
     *  pre: the column given is within the boundaries of the board<br>
     *  post: the game has been updated to include the human players move
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

            System.out.println("HINT: "+human.hintMove(simulator.getBoard()));

            human.makeMove(column);
        }
        simulator.gameUpdate();
    }

    /**
     * A function which will ask the simulator to provide a hint for the human
     * players next move <p>
     * pre: the game state is valid <br>
     * post: the simulator has been called to provie a hint to the player
     */
    public void provideHint()
    {
        simulator.provideHint();
    }

    /**
     * Undoes a move by resetting the Simulator to the old version <p>
     * pre: the game state is valid <br>
     * post: The game has reverted to the state it was in before the last human move, if no
     * moves were yet made the game state has not changed, can also be called if a player 
     * has already won, or the game ended in a draw
     */
    public void undoSimulator(){
        if(undorecord.size() > 0){
            f.getContentPane().removeAll();
            simulator = undorecord.getLast().clone();
            undorecord.removeLast();

            //set the side panel and content buttons
            setColumnButtons();
            setSidePanel();

            BoardRenderer renderer = new BoardRenderer(); 
            //initiate renderer attributes
            renderer.setBoard(simulator.getBoard());
            renderer.setFrame(f);
            renderer.render();
            renderer.setGameMessage(gameMessage); 
            
            simulator.gameUpdate();

            f.pack();
        }
    }
}
