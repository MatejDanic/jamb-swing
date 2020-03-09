package matej.jamb.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import matej.jamb.thread.GameThread;

@Entity
public class Game {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Transient
	private static Integer currentlyRunning;

	@Transient
	private GameThread gameThread;

	@Transient
	private boolean started = false;

	public int getId() {
		return id;
	}
	
	public static boolean startGame(Game game) {
		if (currentlyRunning == null) currentlyRunning = Integer.valueOf(0);

		boolean succesfullyStarted = false;
		if (currentlyRunning <= 5) {
			currentlyRunning++;
			try {
				//				System.out.println("starting thread " + game.id);
				if (!game.isStarted()) {
					game.setStarted(true);
					game.gameThread = new GameThread(game.id);
					game.gameThread.start();
				}
				succesfullyStarted = true;
			} catch (Exception e) {
				//				e.printStackTrace();
				System.out.println("Game " + game.id + " stopped!");
			}
		}
		return succesfullyStarted;
	}

	public static void stopGame(Game game) {
		try {
			game.gameThread.interrupt();
			game.setStarted(false);
		} catch (Exception e) {
			//			e.printStackTrace();
		}
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}
}
