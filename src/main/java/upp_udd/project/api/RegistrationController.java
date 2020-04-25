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
import upp_udd.project.dto.UserDto;
import upp_udd.project.services.RegistrationService;

@RestController
@RequestMapping("/registration")
@AllArgsConstructor
public class RegistrationController {

    private final RuntimeService runtimeService;
    private final TaskService taskService;
    private final RegistrationService registrationService;

    @PostMapping("/register")
    public String startInstance(@RequestBody UserDto userDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("firstName", userDto.getFirstName());
        map.put("lastName", userDto.getLastName());
        map.put("city", userDto.getCity());
        map.put("country", userDto.getCountry());
        map.put("title", userDto.getTitle());
        map.put("email", userDto.getEmail());
        map.put("username", userDto.getUsername());
        map.put("password", userDto.getPassword());
        map.put("scientificFields", userDto.getScientificFields());
        map.put("isReviewer", userDto.getIsReviewer());

        return runtimeService.startProcessInstanceByKey("registration", map).getProcessInstanceId();
    }

    @PostMapping("/confirmRegistration/{hash}")
    public void confirmRegistration(@PathVariable("hash") String hash) {
        registrationService.confirmUser(hash);
    }

    /*@PostMapping("/userSubmit/processInstanceId/{processInstanceId}/{odobren}")
    public void userSubmit(@PathVariable("processInstanceId") String processInstanceId, @PathVariable("odobren") Boolean odobren) {
        List<Task> usertasks = taskService.createTaskQuery()
                                          .processInstanceId(processInstanceId)
                                          .list();

        usertasks.forEach(task -> {
            Map<String, Object> taskVariables = new HashMap<>();
            taskVariables.put("odobren", odobren);
            taskService.complete(task.getId(), taskVariables);
        });
    }

    @GetMapping("/get-tasks/{processInstanceId}")
    public List<TaskRepresentation> getTasks(
            @PathVariable String processInstanceId) {

        List<Task> usertasks = taskService.createTaskQuery()
                                          .processInstanceId(processInstanceId)
                                          .list();

        return usertasks.stream()
                        .map(task -> new TaskRepresentation(
                                task.getId(), task.getName(), task.getProcessInstanceId()))
                        .collect(Collectors.toList());
    }*/

}
