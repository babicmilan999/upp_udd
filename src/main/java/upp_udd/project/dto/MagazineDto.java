package upp_udd.project.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MagazineDto {

    private Long id;
    private String name;
    private String ISSN;
    private List<String> scientificFields;
    private String mainEditor;

}
