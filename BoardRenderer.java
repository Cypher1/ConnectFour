//Basic Renderer for the board
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;

class BoardRenderer extends JPanel implements ActionListener{
    /**
     * 
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
    private int dropDistance;
    private static int delay = 20;

    private Dimension offDimension;
    private Image offImage;
    private Graphics2D g2d;
    private Timer timer;
    private Long lastTime = null;

    public BoardRenderer(){
        super();
        timer = new Timer(delay, this);
        timer.start();// Start the timer here.
    }

    public void setBoard(Board board){
        this.board = board;
        board.addRenderer(this);

        width = startX*2+(sizeX+spacing)*board.getWidth();
        height = startY*2+(sizeY+spacing)*board.getHeight();

        lastX = board.getLastX();
        lastY = board.getLastY();
        dropDistance = DROP_HEIGHT;

        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(width, height));
    }

    /**
    *    Responsible for setting the game message pointer.
    *    @params gameMessage: a pointer to the JLabel that shows 'Game State Messages' in
    *        the GUI
    */
    public void setGameMessage(JLabel gameMessage)
    {
        this.gameMessage = gameMessage;
    }

    public void setFrame(JFrame window){
        this.window = window;

        //create Grid Bag constraints for layout of the window
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = connectFour.BOARD_PLACEMENT[1];
        c.gridx = connectFour.BOARD_PLACEMENT[0];

        //add to JFrame
        this.window.add(this, c);
    }
    
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
                
        int width = board.getWidth();
        int height = board.getHeight();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Integer state = board.getState(x,y);
               
                int xPos = startX+(sizeX+spacing)*x;
                int yPos = startY+(sizeY+spacing)*y;
                boolean dropping = (lastX != null && lastY != null && lastX == x && lastY == y && (dropDistance > 0));

                g2d.setColor( Color.getHSBColor(0, 0, 0) );
                g2d.fillOval(xPos-1, yPos-1, sizeX+2, sizeY+2); 
                                
                if(state != null && !dropping){
                    if(board.isWin(x,y) == state){
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
                    g2d.fillOval(xPos-1, yPos-1, sizeX+2, sizeY+2); 
                    
                    g2d.setColor(Color.getHSBColor((float)((state)*0.18), (float)1.0, (float)1.0) );
                    g2d.fillOval(xPos, yPos, sizeX, sizeY);
                }
            }
        }
        g.drawImage(offImage, 0, 0, this); 

        if(gameMessage != null){
            updateGameMessage( board.getCurrentPlayer(), board.getWinner());
        }
   }
    
    @Override
    public void paint(Graphics g){
        update(g);
    }

    public void render(){
        Graphics g = getGraphics();
        if(g != null){
            paint(g);
        }
    }

    /** 
    *    function responsible for updating play messages each turn
    */
    private void updateGameMessage(int player, Integer win)
    {
        //!!Does not support 3 player mode very well
        String colour;
        if (win != null) player = (player + 1)%2;

        //set the name of the player
        if (player == 0){
            colour = "Red";
        } else if (player == 1){
            colour = "Yellow";
        }else{
            colour = player + "";
        }

        //create the message
        String message;
        if (win != null){
            message = colour + " player wins!!";
            gameMessage.setFont(gameMessage.getFont().deriveFont(23.0f));
        }else{
            message = colour + "'s turn";
        }

        //add the message to the gameMessage JLabel
        gameMessage.setText(message);
        gameMessage.repaint();
    }

    /**
     * Automatically redraws the board
     */
    public void actionPerformed(ActionEvent ev){
        if(ev.getSource()==timer){
            render();// this will call at every 1 second
        }
    }

    public long getLastTime(){
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
