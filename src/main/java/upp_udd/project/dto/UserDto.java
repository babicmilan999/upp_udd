package upp_udd.project.dto;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import upp_udd.project.model.ScientificField;
import upp_udd.project.model.User;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long id;
    private String username;
    private String firstName;
    private String lastName;
    private User.Role role;
    private Set<ScientificField> scientificFields;

}
