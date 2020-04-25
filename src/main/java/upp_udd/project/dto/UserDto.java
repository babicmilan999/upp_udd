package upp_udd.project.dto;

import lombok.Data;

@Data
public class UserDto {

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
