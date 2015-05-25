//A basic board
import java.util.LinkedList;

class BasicBoard implements Board, Cloneable{

    private static final int FIRSTPLAYER = 0;

    private Integer boardState[][];
    private int numPlayers;
    private int currentPlayer;
    private int winlen;
    private Integer lastX;
    private Integer lastY;

    private LinkedList<BoardRenderer> renderers;

    BasicBoard(int numPlayers, int winlen){
        this.winlen = winlen;
        this.numPlayers = numPlayers;
        this.currentPlayer = FIRSTPLAYER;
        this.lastX = null;
        this.lastY = null;

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
        this.lastX = x;
        this.lastY = y;
    }

    public Integer getWinner(){
        if (lastX != null && lastY != null){
            return isWin(lastX, lastY);
        }
        return null;
    }

    public Integer isWin(int startx, int starty){
        for (int dx = -1; dx < 1; dx ++) {
            for (int dy = -1; dy <= 1; dy++) {
                Integer winner = isWin(startx, starty,dx,dy);
                if(winner != null){
                    return winner;
                }
            }
        }

        return null;
    }

    public boolean isFull(){
        boolean isFull = true;
        int x = 0;
        int y = 0;
        while (isFull == true && x < getWidth()){
            for (y = 0; y < getHeight(); y ++){
                System.out.println("checking" + x + y);
                if (boardState[x][y] == null){
                    isFull = false;
                    break;
                }
            }
            x ++;
        }
        return isFull;
    }

    private Integer isWin(int startx, int starty, int dx, int dy){
        if(dx == 0 && dy == 0){
            return null;
        }

        int x = startx;
        int y = starty;
        boolean backTracked = false;
        Integer type = getState(x,y);

        //check that the given coordinate is actually one of the players
        if(type == null){
            return null;
        }
        for(int len=1; len < winlen; len++){
            x+=dx;
            y+=dy;
            if(getState(x,y) != type){
                if(backTracked){
                    return null;
                }
                backTracked = true;
                x = startx;
                y = starty;
                dx *= -1;
                dy *= -1;
                len--;
            }
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
        newBoard.lastX = this.lastX;
        newBoard.lastY = this.lastY;
        return newBoard;
    }
}
