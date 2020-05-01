package upp_udd.project.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.activiti.engine.RuntimeService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import upp_udd.project.dto.MagazineDto;
import upp_udd.project.dto.UserDto;
import upp_udd.project.model.User;
import upp_udd.project.services.MagazineService;
import upp_udd.project.services.UserService;

@RestController
@RequestMapping("/magazine")
@AllArgsConstructor
public class MagazineController {

    private final RuntimeService runtimeService;
    private final UserService userService;
    private final MagazineService magazineService;

    @PostMapping
    public void createMagazine(@RequestBody MagazineDto magazineDto) {
        Map<String, Object> map = new HashMap<>();
        map.put("name", magazineDto.getName());
        map.put("ISSN", magazineDto.getISSN());
        map.put("scientificFields", magazineDto.getScientificFields().stream().collect(Collectors.joining(",")));
        map.put("initiator", SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());

        runtimeService.startProcessInstanceByKey("magazine_creation", map).getProcessInstanceId();
    }

    @GetMapping("/getReviewersAndEditors/magazineId/{magazineId}")
    public Map<User.Role, List<UserDto>> getReviewersAndEditorsForMagazine(@PathVariable("magazineId") Long magazineId) {
        return userService.getReviewersForScientificFields(magazineId);
    }

    @PostMapping("/saveReviewersAndEditors/magazineId/{magazineId}")
    public void saveReviewersAndEditors(@RequestBody Map<User.Role, List<UserDto>> reviewersEditorsMap, @PathVariable("magazineId") Long magazineId) {
        magazineService.saveReviewersAndEditors(reviewersEditorsMap, magazineId);
    }

}
