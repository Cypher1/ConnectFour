import java.util.LinkedList;

public class Simulator implements Runnable
{
	private LinkedList<Player> players;
	private BasicBoard board;

	private int currentPlayer = 0;

	private static final int WINLEN = 4;
	
	Simulator(LinkedList<Player> players){
		gamestart(players);//start the game
		board = new BasicBoard(players.size(), WINLEN);
	}

	public int getCurrentPlayer()
	{
		return this.currentPlayer;
	}

	public int getWinner(){
		return board.getWinner();
	}

	@Override
    public void run() {
    	gameLoop();
    }

	public void gameLoop(){
		while(getWinner() == board.EMPTY){
			//get the move of the player
			Player curr = players.get(currentPlayer);
			System.out.println("YOUR MOVE PLAYER"+(currentPlayer+1));
			int moveXPos = curr.nextMove(board.clone());
			//enact the move			
			boolean legal = board.placeMove(moveXPos);
			
			while(legal == false){
				//System.out.println("Not IMPLEMENTED!!!");	
				moveXPos = curr.nextMove(board.clone());
				legal = board.placeMove(moveXPos);	
			}
			if (legal){
				System.out.println(moveXPos);
			}
			
			//update the board Renderer / UI
			//next turn (assuming that there is no winner)
			currentPlayer = (currentPlayer+1)%players.size();
		}

		System.out.println("WINNER == PLAYER"+(getWinner()+1));
	}

	public BasicBoard getBoard(){
		return board;
	}
	
	public void gamestart(LinkedList<Player> players){//restart or start the game
		this.players = players;
		this.board = new BasicBoard(players.size(), WINLEN);
	}
}
