package upp_udd.project.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import upp_udd.project.model.Article;

@Repository
public interface ArticleRepository extends ElasticsearchRepository<Article, Long> {

}
