package upp_udd.project.dto;

import lombok.Data;

@Data
public class QueryDto {

    private Operator operator;

    private String magazineName;
    private String title;
    private String authorName;
    private String authorSurname;
    private String keyWords;
    private String content;
    private String scientificFields;

    public enum Operator {
        AND, OR
    }

}
