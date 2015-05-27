/** 
 * A Hard Player for Connect Four
 * Uses NegaMax to determine which moves are the best options
 * Still makes some foolish moves when it cannot look far enough into the future of the game
 * Written by Joshua Pratt
 */
public class HardPlayer implements Player {
	private static final int inf = 100000;

    /** 
    * Returns the next move that the player will choose to do <p>
    * pre: the board is valid <br>
    * post: no move has been implemented in the simulator, but a column number has been 
    * returned
    * @param current: this is the current board state, which is used by the AI to determine
    * it's next move
    * @return the int id of the column in which the player wants to make it's move
    */
	public int nextMove (Board current){
		//Initial call for Player A's root node
		int move = 0;
		double moveVal = -inf;
		for(int op = 0; op < current.getWidth(); op++){
			Board moveBoard = current.clone();
			if(moveBoard.placeMove(op)){
				double canWin = -negaMax(moveBoard, 6, -inf, inf,  moveBoard.getCurrentPlayer());
                                canWin += 1-(Math.abs(op-moveBoard.getWidth()/2.0))/moveBoard.getWidth();
				
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

    /** 
    * Calculates a value for the best possible action to be chosen.
    * Calculated while assuming enemy player uses the same algorithm or a close approximation.
    * Uses negaMax with Alpha Beta Pruning. <p>
	* pre: the input given is valid as defined by the negaMax algorithm<br>
	* post: no change is made, but the win value of the current state is returned
    * @param board: the current state of the board
    * @param depth: the number of expansions left for evaluation
    * @param alpha: the best possible win value for the current player
    * @param beta: the best possible win value for the other player
    * @param player: the id of the current player
    * @return the win value of the current state
    */
	private double negaMax(Board board, int depth, double alpha, double beta, int player){
		Integer winner = board.getWinner();//check if the game has ended
		if(depth == 0 || (winner != null && winner != -1)){
			if(depth == 0){
				return heuristic(board);
			}

			if(winner == player){
				return depth;
			}
			return -depth;
		}

		double bestValue = -10000;
		for(int op = 0; op < board.getWidth(); op++){
			Board childBoard = board.clone();
			boolean worked = childBoard.placeMove(op);
			if(worked){//is a legal move
				double val = -0.8*negaMax(childBoard, depth - 1, -beta, -alpha, childBoard.getCurrentPlayer());

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
		return bestValue;
	}

    private double heuristic(Board board){
        double score = 0;
        int player = board.getCurrentPlayer();

        for(int x = 0; x < board.getWidth(); x++){
            for(int y = 0; y < board.getHeight(); y++){
                Integer state = board.getState(x,y);
                if(state != null){
                    Integer up = board.getState(x,y-1);
                    Integer right = board.getState(x+1,y);
                    Integer upRight = board.getState(x+1, y-1);
                    Integer downRight = board.getState(x+1, y+1);
                    
                    int numEq = 0;    
                    if(state == up) numEq++;
                    if(state == right) numEq++;
                    if(state == upRight) numEq++;
                    if(state == downRight) numEq++;

                    if(state == player){
                        numEq *= -1;
                    }                  
                    score += numEq;
                }

            }
        }

        return score/4.0;
    }
}
