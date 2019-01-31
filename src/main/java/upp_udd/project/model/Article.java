package upp_udd.project.model;

import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "article_ind", type = "book", shards = 1)
public class Article {

    private Integer id;
    private String name;

}
