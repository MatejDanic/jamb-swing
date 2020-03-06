package matej.jamb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class JambApplication {

	public static void main(String[] args) {
		SpringApplication.run(JambApplication.class, args);
		
		Jamb jamb = new Jamb();
		jamb.start();
	}

}
