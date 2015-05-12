//A basic board
import java.util.LinkedList;

class BasicBoard implements Board, Cloneable{

    public final static int EMPTY = -1;
    
    private int boardState[][];
    private int numPlayers;
    private int currentPlayer;

    private LinkedList<BoardRenderer> renderers;

    BasicBoard(int numPlayers){
    	this.numPlayers = numPlayers;
	this.currentPlayer = 0;

	boardState = new int[getWidth()][getHeight()];

	for(int x=0; x < getWidth(); x++){
	    for(int y=0; y < getHeight(); y++){
		boardState[x][y] = EMPTY;
            }
	}

	renderers = new LinkedList<BoardRenderer>();
    }

    public void addRenderer(BoardRenderer render){
	renderers.add(render);
    }

    public int getWidth(){
        return 7;
    }

    public int getHeight(){
        return 6;
    }

    public int getState(int x, int y){
	if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()){
		return EMPTY;
	}
        return boardState[x][y];

    }

    public boolean placeMove(int xPos){
       	if(xPos >= getWidth()){
		return false;
	}

	for(int y=getHeight()-1; y >= 0; y--){
            if(getState(xPos, y) == EMPTY){
                setBoard(xPos, y, currentPlayer);
                nextPlayer();
		for(BoardRenderer render : renderers){
			render.render();
		}
                return true;
            }
        }
        
        return false;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    public void setCurrentPlayer(int player){
	currentPlayer = player;
    }

    private void nextPlayer(){
        currentPlayer = (currentPlayer+1)%numPlayers;//increment the currentPlayer
    }

    private void setBoard(int x, int y, int player){
        boardState[x][y] = player;
    }

    public BasicBoard clone(){
	BasicBoard newBoard = new BasicBoard(numPlayers);

	newBoard.setCurrentPlayer(getCurrentPlayer());

	for(int x = 0; x < getWidth(); x++){
		for(int y = 0; y < getHeight(); y++){
			newBoard.setBoard(x,y, getState(x,y));
		}
	}

	return newBoard;
    }
}
