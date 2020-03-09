package matej.jamb.network;

import matej.jamb.models.Game;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/games")
public class GameContoller {

    @Autowired
    GameService gameService;

    @PostMapping("")
    public void addGame(@RequestBody Game game) {
        gameService.addGame(game);
    }

    @GetMapping("/{id}")
    public Game getGameById(@PathVariable(value="id") int id) {
        return gameService.getGameById(id);
    }
    
    @GetMapping("")
    public List<Game> getGames() {
        return gameService.getGames();
    }
    
    @GetMapping("/{id}/start")
    public boolean startGameById(@PathVariable(value="id") int id) {
    	return gameService.startGameById(id);
    }
    
    @GetMapping("/{id}/stop")
    public boolean stopGameById(@PathVariable(value="id") int id) {
    	return gameService.stopGameById(id);
    }
}
