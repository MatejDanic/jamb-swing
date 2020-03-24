package matej.jamb.net;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scores")
@CrossOrigin(origins = "http://jamb-remote.herokuapp.com")
public class ScoreController {

	@Autowired
	ScoreService scoreService;
	

	@Scheduled(fixedRate = 86400000)
	public void clearUnfinishedScores() {
		scoreService.clearUnfinishedScores();
	}

	@PostMapping("")
	public int addScore(@RequestBody Score score) {
		return scoreService.addScore(score);
	}

	@GetMapping("/{id}")
	public Score getScoreById(@PathVariable(value="id") int id) {
		return scoreService.getScoreById(id);
	}

	@GetMapping("")
	public List<Score> getScoreList() {
		return scoreService.getScoreList();
	}

	@DeleteMapping("/{id}")
	public void deleteScoreById(@PathVariable(value="id") int id) {
		scoreService.deleteScoreById(id);
	}

	@PutMapping("/{id}")
	public void saveResource(@RequestBody String value, @PathVariable("id") int id) {
		scoreService.saveScore(id, value);
	}

	@GetMapping("/leaderboard")
	public List<Score> getLeaderboard() {
		return scoreService.getLeaderboard(10);
	}
}
