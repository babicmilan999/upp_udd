package upp_udd.project.services;

import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import upp_udd.project.model.ScientificField;
import upp_udd.project.model.User;
import upp_udd.project.repositories.ScientificFieldRepository;
import upp_udd.project.repositories.UserRepository;

@Service
@AllArgsConstructor
@Slf4j
public class RegistrationService {

    private final TaskService taskService;
    private final UserRepository userRepository;
    private final ScientificFieldRepository scientificFieldRepository;
    private final RuntimeService runtimeService;

    public boolean validate(String firstName,
                            String lastName,
                            String city,
                            String country,
                            String title,
                            String email,
                            String username,
                            String password,
                            Boolean isReviewer,
                            String scientificFields) {
        //TODO implement validation
        log.info("Is valid: {}", true);
        return true;
    }

    @Transactional
    public void sendConfirmationEmail(String firstName,
                                      String lastName,
                                      String city,
                                      String country,
                                      String title,
                                      String email,
                                      String username,
                                      String password,
                                      Boolean isReviewer,
                                      String scientificFields) {
        final String hash = generateHash();
        log.info("Send confirmation email and persist with hash: {}", hash);
        userRepository.save(User.builder()
                                .firstName(firstName)
                                .lastName(lastName)
                                .city(city)
                                .country(country)
                                .title(title)
                                .username(username)
                                .password(password)
                                .email(email)
                                .role(User.Role.AUTHOR)
                                .hash(hash)
                                .scientificFields(extractScientificFields(scientificFields))
                                .build());
        //TODO send email
    }

    @Transactional
    public void setConfirmed(String username) {
        log.info("Setting confirmed for username {}", username);
        User user = userRepository.findByUsername(username);
        user.setConfirmed(true);
        userRepository.save(user);
    }

    @Transactional
    public void confirmUser(String hash) {
        log.info("Confirming user for hash: {}", hash);
        userRepository.findByHash(hash).ifPresent(user -> {
            user.setConfirmed(true);
            userRepository.save(user);
            final Task task = taskService.createTaskQuery().taskAssignee(user.getUsername()).list().iterator().next();
            taskService.complete(task.getId());
        });
    }

    @Transactional
    public void confirmReviewer(String taskId, String adminUsername, Boolean approved, String username) {
        taskService.claim(taskId, adminUsername);
        if (approved) {
            User user = userRepository.findByUsername(username);
            user.setRole(User.Role.REVIEWER);
            userRepository.save(user);
        }

        taskService.complete(taskId);
    }

    private Set<ScientificField> extractScientificFields(String scientificFields) {
        return Arrays.stream(scientificFields.split(","))
                     .map(scientificFieldRepository::findByValue)
                     .collect(Collectors.toSet());
    }

    private String generateHash() {
        return UUID.randomUUID().toString();
    }

}
