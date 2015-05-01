/**
An interface for Boards

Allows the game to use different boards
*/
public interface Board{

	public int getWidth();
	public int getHeight();
	public int getCurrentPlayer();
	public int getState(int x, int y);
	public boolean placeMove(int xPos);
	
}
