import java.util.Random;

public class EasyPlayer implements Player {
	Random generator;

	public EasyPlayer() {
		generator = new Random();
	}

	public int nextMove (Simulator current){
		return generator.nextInt(8);
	}
}