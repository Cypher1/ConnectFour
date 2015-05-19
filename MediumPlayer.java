/** A medium player for connect four 
**/
public class MediumPlayer implements Player {
	/** Returns the next move the player would do 
	**/
	public int nextMove (Board current){
		int inf = Integer.MIN_VALUE;
		int move = 0;
		double moveVal = inf;
		for(int op = 0; op < current.getWidth(); op++){
			Board moveBoard = current.clone();		
			if(moveBoard.placeMove(op)){
				double canWin = negaMax(moveBoard, 4, current.getCurrentPlayer());
					if(canWin > moveVal){
						moveVal = canWin;
						move = op;
					}
			}
		}
		return move;
	}
	/** Calculates a value for the best possible moves
	Uses negamax 
	**/
	
	private double negaMax(Board board, int depth, int player){
		Integer winner = board.getWinner();//check if the game has ended
		double bestValue = Integer.MIN_VALUE;
		if(depth == 0 || winner != null){
			if(depth == 0){
				return 0.1;
			}

			if(winner == player){
				return -depth;
			}
			return depth;
		}

		for(int op = 0; op < board.getWidth(); op++){
			Board childBoard = board.clone();
			boolean worked = childBoard.placeMove(op);
			if(worked){//is a legal move
				double val = 0.3*negaMax(childBoard, depth - 1,  childBoard.getCurrentPlayer());
				bestValue = Math.max(bestValue, val);
			}
		}
		return -bestValue;
	}
}
