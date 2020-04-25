package upp_udd.project;

import java.util.Arrays;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.AllArgsConstructor;
import upp_udd.project.model.ScientificField;
import upp_udd.project.repositories.ScientificFieldRepository;

@SpringBootApplication
@AllArgsConstructor
public class ProjectApplication {

	private final ScientificFieldRepository scientificFieldRepository;

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
	}

}

