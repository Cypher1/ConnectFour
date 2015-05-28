import java.util.LinkedList;

public class Simulator
{
    private LinkedList<Player> players;
    private Board board;

    private int currentPlayer = 0;

    private static final int WINLEN = 4;

    /**
    * A constructor for a new simulator. <p>
    * pre: the linkedlist of players is non-empty, and boardwidth and height are not 0<br>
    * post: a new simulator has been created with the given inputs
    *
    * @param players: a linkedlist of players which will be playing the game
    * @param boardWidth: the width that the board of the game will be
    * @param boardHeight: the height that the board of the game will be
    */    
    Simulator(LinkedList<Player> players, int boardWidth, int boardHeight){
        this.players = players;
        this.board = new Board(players.size(), WINLEN, boardWidth, boardHeight);
    }

    /**
    * A getter for the current player
    * @return the id of the current player
    */
    public int getCurrentPlayer(){
        return this.currentPlayer;
    }

    /**
    * A getter for the winner of the game
    * @return the id of the winning player, or null if no win has been made
    */
    public Integer getWinner(){
        return board.getWinner();
    }

    /**
    * A getter for the board this simulator is based on
    * @return the board in it's current state
    */
    public Board getBoard(){
        return board;
    }

    /**
    * This updates the game by one move. The outcome of that move is also dealt with, and 
    * simulator is left in a state which allows the next move to be made by a different player <p>
    * pre: the game is valid<br>
    * post: game has been updated by one move, a move has been made and whether that move has generated
    * a win or draw has been dealt with
    */
    public void gameUpdate(){
        Player curr = players.get(currentPlayer);
        boolean legal = true;
        if(getWinner() == null && !board.isFull()){
            //get the move of the player
            curr = players.get(currentPlayer);
            System.out.println("YOUR MOVE PLAYER"+(currentPlayer+1));
            int moveXPos = curr.nextMove(board.clone());
            //enact the move            
            legal = board.placeMove(moveXPos);
            if(legal){  
                System.out.println(moveXPos);
                //update the board Renderer / UI
                //next turn (assuming that there is no winner)
                currentPlayer = (currentPlayer+1)%players.size();
                if(getWinner() != null){
                    System.out.println("WINNER == PLAYER"+(getWinner()+1));
                }       
            }
        }

        
        if (!board.isFull() && getWinner() == null && !(players.get(currentPlayer) instanceof HumanPlayer)){//call again for AI players
            gameUpdate();
        }           

        if (board.isFull()){
            System.out.println("PLAYERS TIED");
        } else if (getWinner() != null){
            System.out.println("WINNER == PLAYER"+(getWinner()+1));
        }
    }
    
    public Simulator clone(){
        Simulator sim = new Simulator(this.players, this.getBoard().getWidth(), this.getBoard().getHeight());
        sim.board = this.board.clone();
        return sim;
    }

    /**
     * A function that will provide the human player with a hint for the next move. 
     */
    public void provideHint()
    {
        Player curr = players.get(currentPlayer);
        if (curr instanceof HumanPlayer){
            HumanPlayer human = (HumanPlayer)curr;
            int hintPos = human.hintMove(board);
            board.provideHint(hintPos);
        }
    }
}
