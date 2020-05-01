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
import upp_udd.project.common.TaskId;
import upp_udd.project.dto.MagazineApprovalDto;
import upp_udd.project.dto.ReviewerApprovalDto;
import upp_udd.project.services.MagazineService;
import upp_udd.project.services.RegistrationService;

@RestController
@RequestMapping("/admin")
@AllArgsConstructor
@Slf4j
public class AdminController {

    private final TaskService taskService;
    private final RuntimeService runtimeService;
    private final RegistrationService registrationService;
    private final MagazineService magazineService;

    @GetMapping("/reviewerApproval")
    public List<ReviewerApprovalDto> getAllReviewerApproval() {
        return mapToReveiwerApproval(taskService.createTaskQuery()
                                                .taskCandidateGroup("administrators")
                                                .list()
                                                .stream()
                                                .filter(task -> task.getTaskDefinitionKey().equals(TaskId.REVIEWER_APPROVAL.getTaskId()))
                                                .collect(Collectors.toList()));
    }

    @GetMapping("/magazineApproval")
    public List<MagazineApprovalDto> getAllMagazineApprovals() {
        return mapToMagazineApproval(taskService.createTaskQuery()
                                                .taskCandidateGroup("administrators")
                                                .list()
                                                .stream()
                                                .filter(task -> task.getTaskDefinitionKey().equals(TaskId.MAGAZINE_APPROVAL.getTaskId()))
                                                .collect(Collectors.toList()));
    }

    @PostMapping("/claimAndComplete/taskId/{taskId}/user/{username}/approved/{approved}")
    public void claimAndComplete(@PathVariable("taskId") String taskId, @PathVariable("approved") Boolean approved, @PathVariable("username") String username) {
        final String adminUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("User: {} claiming and completing task: {}", adminUsername, taskId);
        registrationService.confirmReviewer(taskId, adminUsername, approved, username);
    }

    @PostMapping("completeReviewerApproval/taskId/{taskId}/approved/{approved}")
    public void claimAndCompleteMagazineApproval(@PathVariable("taskId") String taskId, @PathVariable("approved") Boolean approved) {
        final String adminUsername = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        log.info("User: {} claiming and completing task: {}", adminUsername, taskId);
        magazineService.approveMagazine(adminUsername, approved, taskId);
    }

    private List<MagazineApprovalDto> mapToMagazineApproval(List<Task> tasks) {
        return tasks.stream()
                    .map(task -> MagazineApprovalDto.builder()
                                                    .taskId(task.getId())
                                                    .magazineId((Long) runtimeService.getVariable(task.getProcessInstanceId(), "magazineId"))
                                                    .taskName(task.getName())
                                                    .build())
                    .collect(Collectors.toList());
    }

    private List<ReviewerApprovalDto> mapToReveiwerApproval(List<Task> tasks) {
        return tasks.stream()
                    .map(task -> ReviewerApprovalDto.builder()
                                                    .id(task.getId())
                                                    .username(runtimeService.getVariable(task.getProcessInstanceId(), "username").toString())
                                                    .taskName(task.getName())
                                                    .build())
                    .collect(Collectors.toList());
    }

}
