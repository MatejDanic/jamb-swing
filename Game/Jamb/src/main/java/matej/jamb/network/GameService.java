package matej.jamb.network;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import matej.jamb.models.Game;

@Service
public class GameService {

	@Autowired
    GameRepository gameRepo;


    public void addGame(Game game) {
    	gameRepo.save(game);
    }

    public Game getGameById(int id) {
        return gameRepo.findById(id).get();
    }

	public boolean startGameById(int id) {
		if (gameRepo.findById(id).isPresent()) {
			Game.startGame(gameRepo.getOne(id));
//			gameRepo.getOne(id).startGame();
			return true;
		}
		return false;
	}
	
	public boolean stopGameById(int id) {
		if (gameRepo.findById(id).isPresent()) {
			Game.stopGame(gameRepo.getOne(id));
//			gameRepo.getOne(id).startGame();
			return true;
		}
		return false;
	}

	
	public List<Game> getGames() {
		return gameRepo.findAll();
	}
}
