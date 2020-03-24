package matej.jamb.net;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameController {
	
	@Autowired
    CachingService cachingService;
    
    @RequestMapping("/bjamb")
    public String index() {
        return "index";
    }
    
    @Scheduled(fixedRate = 86400000)
    public void evictAllCaches() {
    	cachingService.evictAllCaches();
    }
}
