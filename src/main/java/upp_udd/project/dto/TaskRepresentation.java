package upp_udd.project.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TaskRepresentation {

    private String id;
    private String name;
    private String processInstanceId;

}
