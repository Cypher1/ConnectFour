//A basic board
import java.util.LinkedList;

class Board implements Cloneable{

    private static final int FIRSTPLAYER = 0;

    private int width;
    private int height;
    private Integer boardState[][];
    private int numPlayers;
    private int currentPlayer;
    private int winlen;
    private Integer lastX;
    private Integer lastY;

    private LinkedList<BoardRenderer> renderers;

    /**
    * A constructor for the board class. <p>
    *
    *pre: Given a number of players and a win lenght greater than 0<br>
    *post: A new board has been created with the given number of players and winning
    *length
    */
    Board(int numPlayers, int winlen, int width, int height){
        this.winlen = winlen;
        this.numPlayers = numPlayers;
        this.currentPlayer = FIRSTPLAYER;
        this.lastX = null;
        this.lastY = null;

        this.width = width;
        this.height = height;

        boardState = new Integer[getWidth()][getHeight()];

        for(int x=0; x < getWidth(); x++){
            for(int y=0; y < getHeight(); y++){
            boardState[x][y] = null;
            }
        }

        renderers = new LinkedList<BoardRenderer>();
    }

    /**
    * Adds a renderer to the board type, so that the board can be rendered. <p>
    * 
    * pre: board is valid as of yet <br>
    * post: the renderer has been added to the list of renderers 
    * @param render: the renderer which is to be added to the board
    */
    public void addRenderer(BoardRenderer render){
       renderers.add(render);
    }

    /**
     * Gets the list of Renderers from the board
     * pre: the board is valid <br>
     * post: the renderers have been returned as a list
     */
    public LinkedList<BoardRenderer> getRenderers()
    {
        return (LinkedList<BoardRenderer>) renderers.clone();
    }

    /**
    * A getter for the width of the board.
    * @return: the integer value of the width of the board
    */
    public int getWidth(){
        return width;
    }

    /**
    * A getter for the height of the board.
    * @return: the integer value of the height of the board
    */
    public int getHeight(){
        return height;
    }

    /**
    * A getter for the state at given coordinates
    *
    * @return the integer id of the player who occopies the given coordinates, or null
    * if neither player has occupied the space
    * @param x: the column number to check
    * @param y: the row number to check
    */
    public Integer getState(int x, int y){
        if(x < 0 || x >= getWidth() || y < 0 || y >= getHeight()){
            return null;
        } else {
            return boardState[x][y];
        }
    }

    /**
    * A getter for the X coordinate of the last move made
    * @return : the Y coordinate of the last move made or null if no move has yet been made
    */
    public Integer getLastX () {
        return lastX;
    }

    /**
    * A getter for the Y coordinate of the last move made
    * @return : the Y coordinate of the last move made or null if no move has yet been made
    */
    public Integer getLastY () {
        return lastY;
    }

    /**
    * This function is used to place a move. When called the player which is set as the 
    * current player, will place a token in the lowest free spot available in the
    * given column <p>
    * pre: the board is valid so far<br>
    * post: the board now has saved the player as having moved in the boardState
    * @return a boolean value as to whether the move was placed
    * @param xPos: the column in which the current player is to make a move
    */
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

    /**
    * A getter for the current player id
    * @return an int id of the player whose turn it is currently as saved by the board
    */
    public int getCurrentPlayer(){
        return currentPlayer;
    }

    /**
    * A setter for setting whose turn it currently is <p>
    * pre: the id given is a valid one<br>
    * post: current player has now been set as the given player id
    * @param player: an int id for the player whose is to be set
    */
    public void setCurrentPlayer(int player){
        currentPlayer = player;
    }

    /**
    * A method of setting the currentPlayer to the next one as saved by the 
    * arrays of players.<p>
    * pre: the array of players has not been corrupted <br>
    * post: the current play is now the next one in the array, or the first if the 
    * previous player was the last one
    */
    private void setNextPlayer(){
        currentPlayer = (currentPlayer+1)%numPlayers;//increment the currentPlayer
    }

    /**
    * This function is used to set a specific set of coordinates on the board as being
    * occupied by a given player (or neither)<p>
    * pre: the given player id is valid<br>
    * post: the coordinates have now been set as being occupied by the player that was
    * given
    * @param x: the column number to be set
    * @param y: the row number to be set
    * @param player: the id of the player which is to occupy the space
    */
    private void setBoard(int x, int y, Integer player){
        boardState[x][y] = player;
        this.lastX = x;
        this.lastY = y;
    }

    /**
    * A getter for the player who has won given the current board state
    * @return the player id of the player who has won, or null if there is no winner
    */
    public Integer getWinner(){
        if (lastX != null && lastY != null){
            return isWin(lastX, lastY);
        }
        return null;
    }

    /**
    * A function to check whether a given token has generated a win<p>
    * pre: the board is correct <br>
    * post: no change is made
    * @param startx: the first column number to check
    * @param starty: the first row number to check
    * @return the player id of whose win it is, or null if there is no winner 
    */
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

    /**
    * A function to check whether a given token has generated a win in a given direction<p>
    * pre: the board is correct <br>
    * post: no change is made
    * @param startx: the first column number to check
    * @param starty: the first row number to check
    * @param dx: the change in the x value, producing the direction in which the 
    * tokens are to be checked
    * @param dy: the change in the y value to be made, producing the direction in which
    * the tokens are to be checked
    * @return the player id of whose win it is, or null if there is no winner 
    */
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

    /**
    * Checks whether the board is currently full <p>
    * @return a boolean value as to whether the board is full or not
    */
    public boolean isFull(){
        boolean isFull = true;
        int x = 0;
        int y = 0;
        while (isFull == true && x < getWidth()){
            for (y = 0; y < getHeight(); y ++){
                if (boardState[x][y] == null){
                    isFull = false;
                    break;
                }
            }
            x ++;
        }
        return isFull;
    }

    /**
    * Overrides the object clone function
    */
    public Board clone(){
       Board newBoard = new Board(numPlayers, winlen, width, height);

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
