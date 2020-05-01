package upp_udd.project.services;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import upp_udd.project.common.TaskId;
import upp_udd.project.dto.MagazineDto;
import upp_udd.project.dto.UserDto;
import upp_udd.project.model.Magazine;
import upp_udd.project.model.ScientificField;
import upp_udd.project.model.User;
import upp_udd.project.repositories.MagazineRepository;
import upp_udd.project.repositories.ScientificFieldRepository;

@Service
public class MagazineService {

    private final MagazineRepository magazineRepository;
    private final ScientificFieldRepository scientificFieldRepository;
    private final UserService userService;
    private final TaskService taskService;
    private final RuntimeService runtimeService;

    public MagazineService(MagazineRepository magazineRepository,
                           ScientificFieldRepository scientificFieldRepository,
                           @Lazy UserService userService,
                           TaskService taskService,
                           RuntimeService runtimeService) {
        this.magazineRepository = magazineRepository;
        this.scientificFieldRepository = scientificFieldRepository;
        this.userService = userService;
        this.taskService = taskService;
        this.runtimeService = runtimeService;
    }

    @Transactional
    public Long persistMagazine(String name, String ISSN, String scientificFields, String initiator) {
        return magazineRepository.save(Magazine.builder()
                                        .name(name)
                                        .ISSN(ISSN)
                                        .mainEditor(userService.findByUsername(initiator))
                                        .scientificFields(extractScientificFields(scientificFields)).build())
                                 .getId();
    }

    @Transactional(readOnly = true)
    public List<MagazineDto> getAllUnvalidatedMagazines() {
        return magazineRepository.findAllByStatus(Magazine.Status.INACTIVE)
                                 .stream()
                                 .map(this::convert)
                                 .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Magazine findById(Long id) {
        return magazineRepository.findById(id).get();
    }

    @Transactional
    public void saveReviewersAndEditors(Map<User.Role, List<UserDto>> reviewersEditorsMap, Long magazineId) {
        Task task = taskService.createTaskQuery().taskAssignee(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString())
                               .list()
                               .stream()
                               .filter(task1 -> task1.getTaskDefinitionKey().equals(TaskId.SELECT_REVIEWERS_AND_EDITORS.getTaskId()))
                               .filter(task1 -> runtimeService.getVariable(task1.getProcessInstanceId(), "magazineId").equals(magazineId))
                               .findFirst()
                               .get();

        Magazine magazine = findById(magazineId);

        final Set<User> users = reviewersEditorsMap.values().stream().flatMap(Collection::stream)
                                                     .map(userDto -> userService.findByUsername(userDto.getUsername())).collect(Collectors.toSet());
        magazine.setEmployees(users);
        magazineRepository.save(magazine);

        taskService.complete(task.getId());

    }

    @Transactional
    public void approveMagazine(String admin, Boolean valid, String taskId) {
        taskService.claim(taskId, admin);

        Map<String, Object> map = new HashMap<>();
        map.put("valid", valid);
        taskService.complete(taskId, map);
    }

    @Transactional
    public void activate(Long magazineId) {
        Magazine magazine = findById(magazineId);
        magazine.setStatus(Magazine.Status.ACTIVE);
        magazineRepository.save(magazine);
    }

    private MagazineDto convert(Magazine magazine) {
        return MagazineDto.builder()
                          .id(magazine.getId())
                          .ISSN(magazine.getISSN())
                          .mainEditor(magazine.getMainEditor().getUsername())
                          .scientificFields(magazine.getScientificFields().stream().map(ScientificField::getValue).collect(Collectors.toList()))
                          .build();
    }

    private Set<ScientificField> extractScientificFields(String scientificFields) {
        return Arrays.stream(scientificFields.split(","))
                     .map(scientificFieldRepository::findByValue)
                     .collect(Collectors.toSet());
    }

}
