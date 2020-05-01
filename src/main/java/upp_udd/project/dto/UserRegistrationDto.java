package upp_udd.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    private Long id;
    private String firstName;
    private String lastName;
    private String city;
    private String country;
    private String title;
    private String email;
    private String scientificFields;
    private String username;
    private String password;
    private Boolean isReviewer;

}
