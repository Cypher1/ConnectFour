//Basic Renderer for the board
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;
import java.util.LinkedList;

class BoardRenderer extends JPanel implements ActionListener{
    /**
     * A renderer for the Board<br>
     * Uses a JPanel and Graphics2D to draw a Board for the UI
     */
    private static int DROP_HEIGHT = 500;
    private static final long serialVersionUID = -7656929072530099542L;
    private Board board = null;
    private JFrame window;
    private JLabel gameMessage;
    private int width;
    private int height;

    private int startX = 10;
    private int startY = 10;
    
    private int sizeX = 60;
    private int sizeY = 60;
    
    private int spacing = 5;

    private Integer lastX = null;
    private Integer lastY = null;
    private LinkedList<Integer> lastXs = new LinkedList<>();
    private LinkedList<Integer> lastYs = new LinkedList<>();
    private int dropDistance;
    private static int delay = 20;
    private Integer hint = null;

    private Dimension offDimension;
    private Image offImage;
    private Graphics2D g2d;
    private Timer timer;
    private Long lastTime = null;

    private static final int FLASH_SPEED = 30;
    private int flashCounter = 0;

    public BoardRenderer(){
        super();
        timer = new Timer(delay, this);
        timer.start();// Start the timer here.
    }

    /**
     * Updates the board the needs to be drawn (and updates the list of moves that have been taken
     * pre: board must be a valid board
     * post: the board will be updated and the list of lastXs and lastYs will contain the last move
     * @param board the new board that need to be renderered
     */
    public void setBoard(Board board){
        this.board = board;
        board.addRenderer(this);

        width = startX*2+(sizeX+spacing)*board.getWidth();
        height = startY*2+(sizeY+spacing)*board.getHeight();
        
        hint = null;
        lastXs.add(board.getLastX());
        lastYs.add(board.getLastY());

        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(width, height));
    }

    /**
     * Responsible for setting the game message pointer.
     * @param gameMessage: a pointer to the JLabel that shows 'Game State Messages' in
     * the GUI
     */
    public void setGameMessage(JLabel gameMessage)
    {
        this.gameMessage = gameMessage;
    }

    /**
     * Adds the renderer to its window
     * @param window The window to draw into
     */
    public void setFrame(JFrame window){
        this.window = window;
        dropDistance = 0;//do not drop when undoing or restarting
        //create Grid Bag constraints for layout of the window
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = connectFour.BOARD_PLACEMENT[1];
        c.gridx = connectFour.BOARD_PLACEMENT[0];

        //add to JFrame
        this.window.add(this, c);
    }
    
    @Override
    public void update(Graphics g) {
        if(getSize().width == 0 || getSize().height == 0){
            return;
        }

        if ( (g2d == null) || (!offDimension.equals(getSize()))) {
            offDimension = getSize();
            offImage = createImage(getSize().width, getSize().height);
            g2d = (Graphics2D) offImage.getGraphics();
        }  
        
        long dt = getLastTime();

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
        g2d.setColor(new Color(0, 0, 200));//set the background color
        g2d.fillRect(0, 0, width, height);

        if(dropDistance != 0){
            dropDistance -= 25*dt/delay;
            if(dropDistance < 0){
                dropDistance = 0;
            }
        } 

        if(dropDistance == 0 && lastXs.size() > 1){
            lastXs.removeFirst();
            lastYs.removeFirst();
            lastX = lastXs.getFirst();
            lastY = lastYs.getFirst();
            dropDistance = DROP_HEIGHT;
        }

        int width = board.getWidth();
        int height = board.getHeight();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Integer state = board.getState(x,y);
               
                int xPos = startX+(sizeX+spacing)*x;
                int yPos = startY+(sizeY+spacing)*y;
                boolean dropping = (dropDistance > 0);
                boolean hidden = false;

                if(dropping){//check if this is one of the ones being dropped
                    if(lastX != null && lastX == x && lastY != null && lastY == y){
                        dropping = true;
                        hidden = true;
                    }
                    else{
                        dropping = false;
                        for(int i=0; i < lastXs.size(); i++){
                            if(lastXs.get(i) == x && lastYs.get(i) == y){
                                hidden = true;
                                break;
                            }
                        }
                    }
                }

                int lineWidth = 2;
                g2d.setColor( Color.getHSBColor(0, 0, 0) );
                g2d.fillOval(xPos-lineWidth, yPos-lineWidth, sizeX+2*lineWidth, sizeY+2*lineWidth); 
       
                boolean flashing = false;
                if(!dropping){
                    if(state == null && hint != null && hint == x && (y+1 == height || board.getState(x,y+1) != null)){
                        if(flashCounter < FLASH_SPEED){
                            state = board.getCurrentPlayer();
                            flashing = true;
                        }
                        if(flashCounter > 2*FLASH_SPEED){
                            flashCounter = 0;
                        }
                    }
                }

                if(state != null && !hidden || flashing){
                    if(board.isWin(x,y) == state || flashing){
                        g2d.setColor(Color.white);
                        g2d.fillOval(xPos, yPos, sizeX, sizeY);
                        g2d.setColor(Color.getHSBColor((float)((state)*0.18), (float)1.0, (float)1.0) );
                        g2d.fillOval(xPos+5, yPos+5, sizeX-10, sizeY-10);
                        continue;
                    }
                    g2d.setColor(Color.getHSBColor((float)((state)*0.18), (float)1.0, (float)1.0) );
                    g2d.fillOval(xPos, yPos, sizeX, sizeY);
                } else {
                    g2d.setColor(Color.white);
                    g2d.fillOval(xPos, yPos, sizeX, sizeY);
                }
                
                if(dropping){
                    yPos -= dropDistance;
                    g2d.setColor( Color.getHSBColor(0, 0, 0) );
                    g2d.fillOval(xPos-lineWidth, yPos-lineWidth, sizeX+2*lineWidth, sizeY+2*lineWidth); 
                    
                    g2d.setColor(Color.getHSBColor((float)((state)*0.18), (float)1.0, (float)1.0) );
                    g2d.fillOval(xPos, yPos, sizeX, sizeY);
                }
            }
        }


        if(gameMessage != null)
            updateGameMessage( board.getCurrentPlayer(), board.getWinner());

        g.drawImage(offImage, 0, 0, this); 
   }
    
    @Override
    public void paint(Graphics g){
        update(g);
    }

    /**
     * Requests a redraw of the board
     */
    public void render(){
        Graphics g = getGraphics();
        if(g != null){
            paint(g);
        }
    }

    /** 
    *    function responsible for updating play messages each turn
    */
    public void updateGameMessage(int player, Integer win)
    {
        //will be the opposite player if there is a winner
        if (win != null) player = (player + 1)%2;

        //set the name of the player
        String colour;
        if (player == 0){
            colour = "Red";
        } else {
            colour = "Yellow";
        }

        //create the message
        String message = "<html><div style=\"text-align: center;\">";
        if (win != null){
            message += colour + " player<p>wins!!!";
            gameMessage.setFont(gameMessage.getFont().deriveFont(23.0f));
        }else{
            message += colour + "'s<p>turn";
        }

        if(board.isFull()){
            message = "Draw";
        }

        message += "</div></html>";

        //add the message to the gameMessage JLabel
        gameMessage.setText(message);
        
    }

    /**
     * Responsible for updating the hint to be rendered as a suggestion to the human player.
     * Should be called by the Board when a hint is requested 
     * @param col : is the column of the hint 
     */
    public void provideHint(int col){
        hint = col;
        flashCounter = 0;
    }

    /**
     * Automatically redraws the board (using a timer)
     */
    public void actionPerformed(ActionEvent ev){
        if(ev.getSource()==timer){
            flashCounter++;
            render();// this will call at every 1 second
        }
    }

    private long getLastTime(){
        long time = new Date().getTime();
        if(lastTime == null){
            lastTime = time;
            return 0;
        }
        long ftime = time - lastTime;
        lastTime = time;
        return ftime;
    }
}
