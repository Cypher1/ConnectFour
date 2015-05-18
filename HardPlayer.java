
public class HardPlayer implements Player {
	private int inf = 100000;

	public int nextMove (Board current){
		//Initial call for Player A's root node
		int move = 0;
		double moveVal = -inf;
		for(int op = 0; op < current.getWidth(); op++){
			Board moveBoard = current.clone();		
			if(moveBoard.placeMove(op)){
				double canWin = negaMax(moveBoard, 10, -inf, inf,  current.getCurrentPlayer());
				System.out.print(op+":"+canWin+". ");
				
				if(canWin > moveVal){
					moveVal = canWin;
					move = op;
				}
			}
		}
		System.out.println();
		System.out.println("CanWin: "+moveVal);
		System.out.println("BestMove: "+move);

		return move;
	}

	public double negaMax(Board board, int depth, double alpha, double beta, int player){
		Integer winner = board.getWinner();//check if the game has ended
		if(depth == 0 || winner != -1){
			if(depth == 0){
				return 0.1;
			}

			if(winner == player){
				return -depth;
			}
			return depth;
		}

		double bestValue = -10000;
		for(int op = 0; op < board.getWidth(); op++){
			Board childBoard = board.clone();
			boolean worked = childBoard.placeMove(op);
			if(worked){//is a legal move
				double val = 0.75*negaMax(childBoard, depth - 1, -beta, -alpha, childBoard.getCurrentPlayer());
				if(val > bestValue){
					bestValue = val;
				}

				if(val > alpha){
					alpha = val;
				}
				if(alpha >= beta){
					break;
				}
			}
		}
		return -bestValue;
	}
}
