package matej.jamb.net;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/scores")
public class ScoreController {

    @Autowired
    ScoreService scoreService;

    @PostMapping("")
    public void addScore(@RequestBody Score score) {
        scoreService.addScore(score);
    }

    @GetMapping("/{id}")
    public Score getScoreById(@PathVariable(value="id") int id) {
        return scoreService.getScoreById(id);
    }
    
    @GetMapping("")
    public List<Score> getScoreList() {
        return scoreService.getScoreList();
    }
}
