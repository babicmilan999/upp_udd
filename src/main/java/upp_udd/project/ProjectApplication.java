package upp_udd.project;

import java.util.Arrays;
import java.util.HashSet;

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
					    .country("Serbia").city("NS").confirmed(true).role(User.Role.ADMIN).build(),
                    User.builder().firstName("Editor1").lastName("Editoric1").email("editor1@ds.com").hash("sadasdasd").username("editor1").password("editor1")
                        .country("Serbia").city("NS").confirmed(true).role(User.Role.EDITOR).build(),
					User.builder().firstName("Editor2").lastName("Editoric2").email("editor2@ds.com").hash("sadadssdasd").username("editor2").password("editor2")
					    .country("Serbia").city("NS").confirmed(true).role(User.Role.EDITOR).build(),
					User.builder().firstName("Reviewer1").lastName("Revieweric1").email("reviewer1@ds.com").hash("ajsdhakfdsa").username("reviewer1").password("reviewer1")
					    .country("Serbia").city("NS").confirmed(true).role(User.Role.REVIEWER).scientificFields(new HashSet<>(Arrays.asList(scientificFieldRepository.findByValue("Programming"), scientificFieldRepository.findByValue("Moleraj")))).build(),
					User.builder().firstName("Reviewer2").lastName("Revieweric2").email("reviewer2@ds.com").hash("aggffakfdsa").username("reviewer2").password("reviewer2")
					    .country("Serbia").city("NS").confirmed(true).role(User.Role.REVIEWER).scientificFields(new HashSet<>(Arrays.asList(scientificFieldRepository.findByValue("Trening"), scientificFieldRepository.findByValue("Moleraj")))).build()));
		}
	}

}

