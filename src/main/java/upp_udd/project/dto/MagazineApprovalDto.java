package upp_udd.project.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MagazineApprovalDto {

    private String taskId;
    private Long magazineId;
    private String taskName;

}
