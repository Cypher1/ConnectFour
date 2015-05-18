
public class MediumPlayer implements Player {
	public int nextMove (Board current){
		return minMax(current, 10, 1);
	}
	//finds the possible moves that the AI can make
	//iterate through states and so how many you winning 
	private int minMax(Board board, int depth, int player) {
		int bestValue = 0;
		int val = 0;
		if (depth == 0 || board.getWinner() != -1) {
			// return heuristic value
			return heuristic(board, player);
		}
		if(player == 1) { 
			bestValue = Integer.MIN_VALUE;
			for(int i = 0; i < board.getWidth(); i++) {
				Board child = board.clone();
				if(child.placeMove(i)) {
					val = -minMax(child, depth - 1, -player);
				}
				bestValue = Math.max(bestValue, val);
			}
			return bestValue;
		} else {
			bestValue = Integer.MAX_VALUE;
			for(int i = 0; i < board.getWidth(); i++) {
				Board child = board.clone();
				if(child.placeMove(i)) {
					val = minMax(child, depth - 1, player);
				}
				bestValue = Math.min(bestValue, val);
			}
			return bestValue;
		}
	}

	private int heuristic (Board board, int player) {
		int heuristicValue = 0;
		// analyse current board state
		// give it a value to determine benefit to player

		for(int i = 0; i < board.getWidth(); i++) {
			int consecutive = 0;
			for(int j = 0; j < board.getHeight(); j++) {
				if(board.getState(i, j) == player) {
					consecutive++;
				} else {
					consecutive = 0;
				}
				if(consecutive == 3) {
					heuristicValue += 100;
				}
				if(consecutive == 4) {
					heuristicValue += 500;
				}
			}
		}


		return heuristicValue;
	}
}
