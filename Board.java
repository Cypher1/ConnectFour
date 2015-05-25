/**
An interface for Boards

Allows the game to use different boards
*/
public interface Board extends Cloneable{

	public int getWidth();
	public int getHeight();
	public int getCurrentPlayer();
	public Integer getState(int x, int y);
	public boolean placeMove(int xPos);
	public Integer getWinner();
	public boolean isFull();
	public void addRenderer(BoardRenderer render);	
	public Board clone();
}
