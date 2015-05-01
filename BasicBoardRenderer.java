//Basic Renderer for the board
import javax.swing.JFrame;

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
		int width = board.getWidth();
		int height = board.getHeight();

		for(int y = 0; y < height; y++){
			for(int x = 0; x < width; x++){
				int state = board.getState(x,y);
				System.out.print(state);
			}
			System.out.println();
		}		

		System.out.println("END RENDER");
	}

}
