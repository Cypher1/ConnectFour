//A basic board
import java.util.LinkedList;

class BasicBoard implements Board, Cloneable{

    private static final int FIRSTPLAYER = 0;

    private Integer boardState[][];
    private int numPlayers;
    private int currentPlayer;
    private int winlen;

    private LinkedList<BoardRenderer> renderers;

    BasicBoard(int numPlayers, int winlen){
        this.winlen = winlen;
        this.numPlayers = numPlayers;
        this.currentPlayer = FIRSTPLAYER;

        boardState = new Integer[getWidth()][getHeight()];

        for(int x=0; x < getWidth(); x++){
            for(int y=0; y < getHeight(); y++){
            boardState[x][y] = null;
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

    public Integer getState(int x, int y){
        if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()){
            return null;
        } else {
            return boardState[x][y];
        }
    }

    public boolean placeMove(int xPos){
        //check if the move is legal
       	if((xPos >= getWidth()) || (xPos == -1)){
            return false;
        }

        for(int y=getHeight()-1; y >= 0; y--){
            if(getState(xPos, y) == null){
                setBoard(xPos, y, currentPlayer);
                setNextPlayer();
                for(BoardRenderer render : renderers){
                    render.setBoard(this.clone());
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

    private void setNextPlayer(){
        currentPlayer = (currentPlayer+1)%numPlayers;//increment the currentPlayer
    }

    private void setBoard(int x, int y, Integer player){
        boardState[x][y] = player;
    }

    public Integer getWinner(){
        for(int x=0; x <= getWidth(); x++){
            for(int y=0; y <= getHeight(); y++){
                Integer winner = isWin(x,y,0,1);
                if(winner != null){
                    return winner;
                }
                winner = isWin(x,y,1,1);
                if(winner != null){
                    return winner;
                }
                winner = isWin(x,y,1,0);
                if(winner != null){
                    return winner;
                }
                winner = isWin(x,y,1,-1);
                if(winner != null){
                    return winner;
                }
            }
        }
        return null;
    }

    private Integer isWin(int x, int y, int dx, int dy){
        Integer type = getState(x,y);

        if(type == null){
            return null;
        }

        for(int len=0; len < winlen; len++){
            if(getState(x,y) != type){
                return null;
            }
            x+=dx;
            y+=dy;
        }
        return type;
    }

    public BasicBoard clone(){
       BasicBoard newBoard = new BasicBoard(numPlayers, winlen);

       newBoard.setCurrentPlayer(getCurrentPlayer());

        for(int x = 0; x < getWidth(); x++){
            for(int y = 0; y < getHeight(); y++){
                newBoard.setBoard(x,y, getState(x,y));
            }
        }

        return newBoard;
    }
}
