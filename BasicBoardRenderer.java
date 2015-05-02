//Basic Renderer for the board
import javax.swing.JFrame;
import javax.swing.JLabel;

class BasicBoardRenderer implements BoardRenderer{
	
	Board board;	
	JFrame window;

	BasicBoardRenderer(){
	}

	@Override
	public void setBoard(Board board){
		this.board = board;
	}

	@Override
	public void setFrame(JFrame window){
		this.window = window;
	}
	
	@Override
	public void render(){
		System.out.println("RERENDER");
		
		String board_as_string = new String("<html>");
		
		//print the board to system.out
		int width = board.getWidth();
		int height = board.getHeight();
		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int state = board.getState(x,y);
				System.out.print(state);
				board_as_string = board_as_string + " " + state;
			}
			board_as_string = board_as_string + "<br>";
			System.out.println();
		}	
		
		board_as_string = board_as_string + "</html>";
		//show the string representation of the board in this.window
		this.window.add(new JLabel(board_as_string));
		this.window.pack();
		
		System.out.println("END RENDER");
	}

}
