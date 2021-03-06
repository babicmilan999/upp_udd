package upp_udd.project.api;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import upp_udd.project.dto.UserRegistrationDto;
import upp_udd.project.services.RegistrationService;

@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public String startInstance(@RequestBody UserRegistrationDto userRegistrationDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", userRegistrationDto.getFirstName());
        map.put("lastName", userRegistrationDto.getLastName());
        map.put("city", userRegistrationDto.getCity());
        map.put("country", userRegistrationDto.getCountry());
        map.put("title", userRegistrationDto.getTitle());
        map.put("email", userRegistrationDto.getEmail());
        map.put("username", userRegistrationDto.getUsername());
        map.put("password", userRegistrationDto.getPassword());
        map.put("scientificFields", userRegistrationDto.getScientificFields());
        map.put("isReviewer", userRegistrationDto.getIsReviewer());

        return runtimeService.startProcessInstanceByKey("registration", map).getProcessInstanceId();
    }

    @PostMapping("/confirmRegistration/{hash}")
    public void confirmRegistration(@PathVariable("hash") String hash) {
        registrationService.confirmUser(hash);
    }

}
