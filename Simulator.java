import java.util.LinkedList;

public class Simulator 
{
	private LinkedList<Player> players;
	private BasicBoard board;

	private int currentPlayer = 0;

	private static final int WINLEN = 4;
	
	Simulator(LinkedList<Player> players){
		gamestart(players);//start the game
		board = new BasicBoard(players.size());
	}

	public int getWinner(){
		for(int x=0; x <= board.getWidth()-WINLEN; x++){
			for(int y=0; y <= board.getHeight(); y++){
				int winner = isWin(x,y,0,1);
				if(winner != board.EMPTY){
					return winner;
				}
				winner = isWin(x,y,1,1);
				if(winner != board.EMPTY){
					return winner;
				}
				winner = isWin(x,y,1,0);
				if(winner != board.EMPTY){
					return winner;
				}
			}
		}
		return board.EMPTY;
	}

	private int isWin(int x, int y, int dx, int dy){
		int type = board.getState(x,y);

		if(type == board.EMPTY){
			return board.EMPTY;
		}
		
		for(int len=0; len < WINLEN; len++){
			if(board.getState(x,y) != type){
				return board.EMPTY;
			}
			x+=dx;
			y+=dy;
		}

		return type;	
	}

	public void gameLoop(){
		while(getWinner() == board.EMPTY){
			//get the move of the player
			Player curr = players.get(currentPlayer);
			System.out.println("YOUR MOVE PLAYER"+(currentPlayer+1));
			int moveXPos = curr.nextMove(board.clone());
			//enact the move
			System.out.println(moveXPos);			
			boolean legal = board.placeMove(moveXPos);
			
			if(legal == false){
				System.out.println("Not IMPLEMENTED!!!");	
				break;
			}
			
			//update the board Renderer / UI
			//next turn (assuming that there is no winner
			currentPlayer = (currentPlayer+1)%players.size();
		}

		System.out.println("WINNER == PLAYER"+(getWinner()+1));
	}

	public BasicBoard getBoard(){
		return board;
	}
	
	public void gamestart(LinkedList<Player> players){//restart or start the game
		this.players = players;
		this.board = new BasicBoard(2);
	}
}
