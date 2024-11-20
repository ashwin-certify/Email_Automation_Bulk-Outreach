package CertifyOS.Email_Integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication

public class EmailIntegrationApplication {

	public static void main(String[] args) {
		SpringApplication.run(EmailIntegrationApplication.class, args);
	}

}
