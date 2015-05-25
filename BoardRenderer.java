//Basic Renderer for the board
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.*;

class BoardRenderer extends JPanel{
    /**
     * 
     */
    private static final long serialVersionUID = -7656929072530099542L;
    private Board board = null;
    private JFrame window;
    private int width;
    private int height;

    
    private int startX = 10;
    private int startY = 10;
    
    private int sizeX = 60;
    private int sizeY = 60;
    
    private int spacing = 5;

    Dimension offDimension;
    Image offImage;
    Graphics2D g2d;
    
    BoardRenderer(){
        super();
    }

    public void setBoard(Board board){
        this.board = board;
        board.addRenderer(this);

        width = startX*2+(sizeX+spacing)*board.getWidth();
        height = startY*2+(sizeY+spacing)*board.getHeight();
        // set a preferred size for the custom panel.
        setPreferredSize(new Dimension(width, height));
        
        render();
    }

    public void setFrame(JFrame window){
        this.window = window;

        //create Grid Bag constraints for layout of the window
        GridBagConstraints c = new GridBagConstraints();
        c.gridy = connectFour.BOARD_PLACEMENT[1];
        c.gridx = connectFour.BOARD_PLACEMENT[0];
        c.gridwidth = connectFour.BOARD_WIDTH;

        //add to JFrame
        this.window.add(this, c);
    }
    
    @Override
    public void update(Graphics g) {
        if(getSize().width == 0 || getSize().height == 0){
        return;
    }

        if ( (g2d == null)
          || (!offDimension.equals(getSize()))) {
            offDimension = getSize();
            offImage = createImage(getSize().width, getSize().height);
            g2d = (Graphics2D) offImage.getGraphics();
        }  
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
        g2d.setColor(new Color(0, 0, 200));//set the background color
        g2d.fillRect(0, 0, width, height);

        int width = board.getWidth();
        int height = board.getHeight();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                Integer state = board.getState(x,y);
                
                g2d.setColor( Color.getHSBColor(0, 0, 0) );
            g2d.fillOval(startX+(sizeX+spacing)*x-1, startY+(sizeY+spacing)*y-1, sizeX+2, sizeY+2);
                
                if(state != null){
            if(board.isWin(x,y) == state){
                        g2d.setColor(Color.white);
                        g2d.fillOval(startX+(sizeX+spacing)*x, startY+(sizeY+spacing)*y, sizeX, sizeY);
                g2d.setColor(Color.getHSBColor((float)((state)*0.18), (float)1.0, (float)1.0) );
            g2d.fillOval(startX+(sizeX+spacing)*x+5, startY+(sizeX+spacing)*y+5, sizeX-10, sizeY-10);
            continue;
                    }
            g2d.setColor(Color.getHSBColor((float)((state)*0.18), (float)1.0, (float)1.0) );
                } else {
                    g2d.setColor(Color.white);
                }
                g2d.fillOval(startX+(sizeX+spacing)*x, startY+(sizeY+spacing)*y, sizeX, sizeY);
            }
        }
        g.drawImage(offImage, 0, 0, this); 
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
}
