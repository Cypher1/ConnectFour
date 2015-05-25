public interface Player {
	/**
	* Given a current board this returns the column in which the player would like to place
	* it's next move <p>
	* pre: the board is valid<br>
	* post: the overall game state is not changed, but a value where a move is to be placed
	* is returned 
	* @param current: the board on which the player is to be making it's move
	* @return the int value of the column in which the move is to be placed
	*/
	public int nextMove (Board current);
}
