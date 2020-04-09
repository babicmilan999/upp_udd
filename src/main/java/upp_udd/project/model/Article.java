package upp_udd.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(indexName = "article", type = "book", shards = 1)
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Article {

    @Id
    private String id;

    private String name;

    private String title;

    private String authorName;

    private String authorSurname;

    private String keyWords;

    @Field(type = FieldType.Text, analyzer = "serbian_analyzer")
    private String content;

    private String scientificField;

    private String filePath;

}
