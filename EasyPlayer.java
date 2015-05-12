import java.util.Random;

public class EasyPlayer implements Player {
	Random generator;

	public EasyPlayer() {
		generator = new Random();
	}

	public int nextMove (Board current){
		return generator.nextInt(current.getWidth()-1);
	}
}
