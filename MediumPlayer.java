public class MediumPlayer implements Player {

	public int nextMove (Board current){
		int inf = Integer.MIN_VALUE;
		//Initial call for Player A's root node
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
	
	private double negaMax(Board board, int depth, int player){
		Integer winner = board.getWinner();//check if the game has ended
		double bestValue = Integer.MIN_VALUE;
		if(depth == 0 || winner != -1){
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