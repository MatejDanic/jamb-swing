package matej.jamb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import matej.jamb.network.JDBC;


@SpringBootApplication
public class JambApplication {

	public static void main(String[] args) {
		SpringApplication.run(JambApplication.class, args);
		JDBC.connectToDatabase();
	}

}
