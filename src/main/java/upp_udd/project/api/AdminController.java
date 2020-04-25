package upp_udd.project.api;

import java.util.List;
import java.util.stream.Collectors;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import upp_udd.project.dto.TaskDto;
import upp_udd.project.services.RegistrationService;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {

    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final RegistrationService registrationService;

    @GetMapping("/getAllUnclaimedTasks")
    public List<TaskDto> getAllUnclaimedTasks() {
        return map(taskService.createTaskQuery().taskCandidateGroup("administrators").list());
    }

    @PostMapping("/claimAndComplete/taskId/{taskId}/user/{username}/approved/{approved}")
    public void claimAndComplete(@PathVariable("taskId") String taskId, @PathVariable("approved") Boolean approved, @PathVariable("username") String username) {
        final String adminUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("User: {} claiming and completing task: {}", adminUsername, taskId);
        registrationService.confirmReviwer(taskId, adminUsername, approved, username);
    }

    private List<TaskDto> map(List<Task> tasks) {
        return tasks.stream()
                    .map(task -> TaskDto.builder()
                                        .id(task.getId())
                                        .username(runtimeService.getVariable(task.getProcessInstanceId(), "username").toString())
                                        .build())
                    .collect(Collectors.toList());
    }

}
