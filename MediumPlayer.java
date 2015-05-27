/** 
* A medium player for connect four 
*/
public class MediumPlayer implements Player {
	
	/** 
	* Returns the next move the player would complete <p>
	* pre: the board given is valid<br>
	* post: no change is made to the board a value is simply returned
	* @param current: the current board state
	* @return the column index in which the player wants to make it's move 
	*/
	public int nextMove (Board current){
		int inf = Integer.MIN_VALUE;
		int move = 0;
		double moveVal = inf;
		for(int op = 0; op < current.getWidth(); op++){
			Board moveBoard = current.clone();		
			if(moveBoard.placeMove(op)){
				double canWin = -negaMax(moveBoard, 4, moveBoard.getCurrentPlayer());
					if(canWin > moveVal){
						moveVal = canWin;
						move = op;
					}
			}
		}
		return move;
	}

	/** 
	* Calculates the next move to be played, uses the negaMax algorithm <p>
	* pre: the values given are valid <br>
	* post: the board state is not changed but a win value is returned
	* @param board: the current board state
	* @param depth: the number of expansions left for evaluation
	* @param player: the index of the current player
	* @return the win value of the current state
	*/
	private double negaMax(Board board, int depth, int player){
		Integer winner = board.getWinner();//check if the game has ended
		double bestValue = Integer.MIN_VALUE;
		if(depth == 0 || winner != null){
			if(depth == 0){
				return 0.1;
			}

			if(winner == player){
				return depth;
			}
			return -depth;
		}

		for(int op = 0; op < board.getWidth(); op++){
			Board childBoard = board.clone();
			boolean worked = childBoard.placeMove(op);
			if(worked){//is a legal move
				double val = -0.3*negaMax(childBoard, depth - 1,  childBoard.getCurrentPlayer());
				bestValue = Math.max(bestValue, val);
			}
		}
		return bestValue;
	}
}
