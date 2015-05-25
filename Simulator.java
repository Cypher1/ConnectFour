import java.util.LinkedList;

public class Simulator implements Runnable
{
    private LinkedList<Player> players;
    private Board board;

    private int currentPlayer = 0;

    private static final int WINLEN = 4;
    
    Simulator(LinkedList<Player> players, int boardWidth, int boardHeight){
        gamestart(players, boardWidth, boardHeight);//start the game
    }

    public int getCurrentPlayer(){
        return this.currentPlayer;
    }

    public Integer getWinner(){
        return board.getWinner();
    }

    public void run() {
        gameUpdate();
    }

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

        
        if (getWinner() == null && !(players.get(currentPlayer) instanceof HumanPlayer)){//call again for AI players
            gameUpdate();
        }           

        if (board.isFull()){
            System.out.println("PLAYERS TIED");
        } else if (getWinner() != null){
            System.out.println("WINNER == PLAYER"+(getWinner()+1));
        }
    }

    public Board getBoard(){
        return board;
    }
    
    public void gamestart(LinkedList<Player> players, int boardWidth, int boardHeight){//restart or start the game
        this.players = players;
        this.board = new Board(players.size(), WINLEN, boardWidth, boardHeight);
    }
}
