//Basic Renderer for the board
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;

class BasicBoardRenderer extends JPanel implements BoardRenderer{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7656929072530099542L;
	/**
	 * 
	 */
	private Board board;
	private JFrame window;
	private int width;
	private int height;

    
    private int startX = 10;
    private int startY = 10;
    
    private int sizeX = 60;
    private int sizeY = 60;
    
    private int spacing = 5;

    
	BasicBoardRenderer(){
		super();
	}

	@Override
	public void setBoard(Board board){
		this.board = board;

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
     	c.gridy = 1;
     	c.gridx = 0;
     	c.gridwidth = 7;

     	//add to JFrame
		this.window.add(this, c);
	}
	
    @Override
    public void paintComponent(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g;
        super.paintComponent(g);
        

        //g.drawString("BLAH", 20, 20);
		g2d.setColor(new Color(0, 0, 200));//set the background color
		g2d.fillRect(0, 0, width, height);

		int width = board.getWidth();
		int height = board.getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int state = board.getState(x,y);
				if(state != 0){
					g2d.setColor(Color.getHSBColor((float)((state%2)*0.18), (float)1.0, (float)1.0) );
				} else {
					g2d.setColor(Color.white);
				}
				g2d.fillOval(startX+(sizeX+spacing)*x, startY+(sizeY+spacing)*y, sizeX, sizeY);
				g2d.setColor( Color.getHSBColor(0, 0, 0) );
				g2d.drawOval(startX+(sizeX+spacing)*x, startY+(sizeY+spacing)*y, sizeX, sizeY);
			}
		}
		
    }
	
	@Override
	public void render(){
		repaint();
	}
}
