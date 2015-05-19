/** A medium player for connect four 
**/
public class MediumPlayer implements Player {
	/** Returns the next move the player would do 
	**/
	public int nextMove (Board current){
<<<<<<< HEAD
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
=======
		return minMax(current, 10, 1);
	}
	//finds the possible moves that the AI can make
	//iterate through states and so how many you winning 
	private int minMax(Board board, int depth, int player) {
		int bestValue, val;
		if (depth == 0 || board.getWinner() != null) {
			// return heuristic value
			return heuristic(board, player);
		}
		if(player == 1) { 
			bestValue = Integer.MIN_VALUE;
			for(int i = 0; i < board.getWidth(); i++) {
				Board child = board.clone();
				if(child.placeMove(i)) {
					val = -minMax(child, depth - 1, -player);
				
					if(bestValue < val){
						bestValue = val;
>>>>>>> cleanBack-end
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
		if(depth == 0 || winner != -1){
			if(depth == 0){
				return 0.1;
			}

<<<<<<< HEAD
			if(winner == player){
				return -depth;
=======
	private int heuristic (Board board, Integer player) {
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
>>>>>>> cleanBack-end
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
