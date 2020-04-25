package upp_udd.project;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.AllArgsConstructor;
import upp_udd.project.model.ScientificField;
import upp_udd.project.model.User;
import upp_udd.project.repositories.ScientificFieldRepository;
import upp_udd.project.repositories.UserRepository;

@SpringBootApplication
@AllArgsConstructor
public class ProjectApplication {

	private final ScientificFieldRepository scientificFieldRepository;
	private final UserRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(ProjectApplication.class, args);
	}

	@EventListener
	public void afterContextLoads(ApplicationReadyEvent applicationReadyEvent) {
		if (scientificFieldRepository.count() == 0) {
			scientificFieldRepository.saveAll(
					Arrays.asList(ScientificField.builder().value("Programming").build(),
					              ScientificField.builder().value("Moleraj").build(),
					              ScientificField.builder().value("Trening").build()));
		}

		if (userRepository.count() == 0) {
			userRepository.saveAll(Arrays.asList(
					User.builder().firstName("Admin1").lastName("Adminkovic1").email("admin1@ds.com").hash("ajsdhaksjd1").username("admin1").password("admin1")
					    .country("Serbia").city("NS").confirmed(true).role(User.Role.ADMIN).build(),
					User.builder().firstName("Admin1").lastName("Adminkovic1").email("admin2@ds.com").hash("ajsdhaksjd2").username("admin2").password("admin2")
					    .country("Serbia").city("NS").confirmed(true).role(User.Role.ADMIN).build()));
		}
	}

}

