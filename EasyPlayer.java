import java.util.Random;
/**
* An easy player which simply drops it's token in a random column
*/
public class EasyPlayer implements Player {
	private Random generator;

	/**
	* A constructor which creates a Random class
	*/
	public EasyPlayer() {
		generator = new Random();
	}

	/**
	* Given a current board this returns the column in which the player would like to place
	* it's next move. Easy player just generates a random column index within the bounds of 
	* the board.<p>
	* pre: the board is valid<br>
	* post: the overall game state is not changed, but a value where a move is to be placed
	* is returned 
	* @param current: the current state of the board, not actually used by this player but
	* is required to implement Player
	* @return the column index which the move should be played in
	*/
	public int nextMove (Board current){
		int move = 0;
        boolean legal = false;
        while(!legal){
            Board test = current.clone();
            move = generator.nextInt(current.getWidth());
            legal = test.placeMove(move);
        }

        return move;
	}
}
