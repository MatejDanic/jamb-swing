package matej.jamb.net;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GameController {
    
    @RequestMapping("/bjamb")
    public String index() {
        return "index";
    }
}
