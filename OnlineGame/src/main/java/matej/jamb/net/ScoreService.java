package matej.jamb.net;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ScoreService {

	@Autowired
    ScoreRepository scoreRepo;


    public void addScore(Score score) {
    	scoreRepo.save(score);
    }

    public Score getScoreById(int id) {
        return scoreRepo.findById(id).get();
    }
	
	public List<Score> getScoreList() {
		return scoreRepo.findAll();
	}
}
