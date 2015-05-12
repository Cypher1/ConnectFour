import javax.swing.JFrame;
/**
An interface for BoardRenderers

Allows the interface to be rendered using different rederers
*/
public interface BoardRenderer{
	public void setBoard(BasicBoard board);
	public void render();
}
