
public class HardPlayer implements Player {
	public int nextMove (Board current){

		//Initial call for Player A's root node
		int canWin = negaMax( current, 20, 1);
		System.out.println(canWin);
		return 0;//move;
	}

	public int negaMax(Board board, int depth, int player){
		int winner = board.getWinner();//check if the game has ended
		if(depth == 0 || winner != -1){
			if(winner == player){
				return 1;
			}
			return -1;
		}

		int bestValue = -10000;
		for(int op = 0; op < board.getWidth(); op++){
			Board childBoard = board.clone();
			boolean worked = childBoard.placeMove(op);
			if(worked){//is a legal move
				int val = -negaMax(childBoard, depth - 1, (player+1)%2);
				if(val > bestValue){
					bestValue = val;
				}
			}
		}
		return bestValue;

	}


}
