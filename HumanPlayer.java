/**
	A human player, this acts as an interface between the simulator and the GUI
*/
public class HumanPlayer implements Player {

	private static final int NO_MOVE = -1;
	private int nextMove;

	/**
		constructor initialises the human player to no moves made. 
	*/
	public HumanPlayer() {
		this.nextMove = NO_MOVE;
	}

	/**
		The function that will get called when a move should be made
		@params an integer between 0 and 6 that represents the column 
			of the next move
	*/
	public void makeMove(int column)
	{
		this.nextMove = column;
	}

	/**
		The function responsible for checking if a move has been made. 
		@params the current board
		@return -1 if no move has been made yet or the number of the 
			column of the move
	*/
	public int nextMove (Board current){
		int move = -1;
		//check move has been made and reinitialise
		if(this.nextMove != NO_MOVE)
			move = this.nextMove;

		this.nextMove = NO_MOVE;
		return move;
	}
}
