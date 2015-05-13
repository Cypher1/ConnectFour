/**
An interface for Boards

Allows the game to use different boards
*/
public interface Board extends Cloneable{

	public int getWidth();
	public int getHeight();
	public int getCurrentPlayer();
	public int getState(int x, int y);
	public boolean placeMove(int xPos);
	public int getWinner();
	public void addRenderer(BoardRenderer render);	
	public Board clone();
}
