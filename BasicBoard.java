//A basic board

class BasicBoard implements Board{

    final static int empty = 0;
    
    int boardState[][];
    int numPlayers;
    int currentPlayer;

    BasicBoard(int numPlayers){
    	this.numPlayers = numPlayers;
	this.currentPlayer = 0;

	boardState = new int[getWidth()][getHeight()];

	for(int x=0; x < getWidth(); x++){
	    for(int y=0; y < getHeight(); y++){
		boardState[x][y] = empty;
            }
	}
    }

    public int getWidth(){
        return 7;
    }

    public int getHeight(){
        return 6;
    }

    public int getState(int x, int y){
        return boardState[x][y];
    }

    public boolean placeMove(int xPos){
       	for(int y=getHeight()-1; y >= 0; y--){
            if(getState(xPos, y) == empty){
                setBoard(xPos, y, currentPlayer);
                nextPlayer();
                return true;
            }
        }
        
        return false;
    }

    public int getCurrentPlayer(){
        return currentPlayer;
    }

    private void nextPlayer(){
        currentPlayer = (currentPlayer+1)%numPlayers;//increment the currentPlayer
    }

    private void setBoard(int x, int y, int player){
        boardState[x][y] = player;
    }
}
