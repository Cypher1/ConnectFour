/** A Hard Player for Connect Four
 * Uses NegaMax to determine which moves are the best options
 * Still makes some foolish moves when it cannot look far enough into the future of the game
 * Written by Joshua Pratt
 */
public class HardPlayer implements Player {
	private int inf = 100000;

    /** Returns the next move that the player will choose to do */
	public int nextMove (Board current){
		//Initial call for Player A's root node
		int move = 0;
		double moveVal = -inf;
		for(int op = 0; op < current.getWidth(); op++){
			Board moveBoard = current.clone();
			if(moveBoard.placeMove(op)){
				double canWin = negaMax(moveBoard, 10, -inf, inf,  current.getCurrentPlayer());
				System.out.print(op+":"+canWin+". ");
		
                canWin += (moveBoard.getWidth()/2.0-Math.abs(op-moveBoard.getWidth()/2.0))/op-moveBoard.getWidth();
                
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

    /** Calculates a value for the best possible action to be chosen (assuming equivalent players)
    *   Uses negaMax with Alpha Beta Pruning
    */
	private double negaMax(Board board, int depth, double alpha, double beta, int player){
		Integer winner = board.getWinner();//check if the game has ended
		if(depth == 0 || (winner != null && winner != -1)){
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
				double val = 0.8*negaMax(childBoard, depth - 1, -beta, -alpha, childBoard.getCurrentPlayer());

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
