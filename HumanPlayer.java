/**
* A class that implements Player, but is controlled by the human. This acts as an interface
* between simulator and the humans
*/
public class HumanPlayer implements Player {

	private static final int NO_MOVE = -1;
	private int nextMove;

	/**
	* A constructor for human player. This sets the next move as no move being made. 
	*/
	public HumanPlayer() {
		this.nextMove = NO_MOVE;
	}

	/**
	* The function that will get called by the UI when a move has been made by the human<p>
	* pre: the given column is within the range of the current board<br>
	* post: nextMove has been set as the given input within the humanplayer class
	* @param column: an integer between 0 and the board width that represents the column of
	* the next move
	*/
	public void makeMove(int column)
	{
		this.nextMove = column;
	}

	/**
	* The function responsible for checking if a move has been made, this will be called
	* by the simulator. <p>
	* pre: none <br>
	* post: either the new move has been returned and nextMove has been set back to NO_MOVE,
	* or NO_MOVE has been returned and no change is made to nextMove
	* @param current: the current board, this is required to conform the HumanPlayer to the
	* Player interface but is not actually used.
	* @return -1 if no move has been made yet or the number of the column of the move
	*/
	public int nextMove (Board current){
		int move = NO_MOVE;
		//check move has been made and reinitialise
		if(this.nextMove != NO_MOVE) {
			move = this.nextMove;
			//reset the nextMove to no move so that the same move does not get played twice
			this.nextMove = NO_MOVE;
		} 
		
		return move;
	}
}
