package matej.jamb.net;

import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ScoreService {

	@Autowired
	ScoreRepository scoreRepo;
	
    public void clearUnfinishedScores() {
		Queue<Score> queue = new LinkedList<>();
    	for (Score score : scoreRepo.findAll()) {
    		if (!score.isFinished()) {
    			queue.add(score);
    		}
    	}
    	scoreRepo.deleteAll(queue);
    }


	public int addScore(Score score) {
		scoreRepo.save(score);
		return score.getId();
	}

	public Score getScoreById(int id) {
		return scoreRepo.findById(id).get();
	}

	public List<Score> getScoreList() {
		return scoreRepo.findAll();
	}

	public void deleteScoreById(int id) {
		scoreRepo.deleteById(id);
	}

	public void saveScore(int id, String value) {
		Score score = scoreRepo.findById(id).get();
		score.setFinished(true);
		score.setValue(Integer.parseInt(value));
		scoreRepo.save(score);	
	}

	public List<Score> getLeaderboard(int max) {
		List<Score> leaderBoard = scoreRepo.findAll();
		Queue<Score> queue = new LinkedList<>();
		Calendar today = Calendar.getInstance();
		today.set(Calendar.HOUR_OF_DAY, 0);
		leaderBoard.forEach(e -> {
			if (!e.isFinished() || !(isSameDay(e.getDate(), today.getTime()))) {
				queue.add(e);
			}
		});
		leaderBoard.removeAll(queue);
		Collections.sort(leaderBoard, new Comparator<Score>() {
		    @Override
		    public int compare(Score s1, Score s2) {
		        return (s2.getValue() - s1.getValue());
		    }
		});
		return leaderBoard.stream().limit(max).collect(Collectors.toList());
	}
	
	public static boolean isSameDay(Date date1, Date date2) {
        if (date1 == null || date2 == null) {
            throw new IllegalArgumentException("The dates must not be null");
        }
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);
        return isSameDay(cal1, cal2);
    }
	
	 public static boolean isSameDay(Calendar cal1, Calendar cal2) {
	        if (cal1 == null || cal2 == null) {
	            throw new IllegalArgumentException("The dates must not be null");
	        }
	        return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA) &&
	                cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
	                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR));
	    }
}
