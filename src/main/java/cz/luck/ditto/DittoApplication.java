package cz.luck.ditto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DittoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DittoApplication.class, args);
	}

}
