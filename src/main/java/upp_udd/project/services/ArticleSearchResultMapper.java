package upp_udd.project.services;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.search.SearchHit;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.stereotype.Component;

import upp_udd.project.model.Article;

@Component
public class ArticleSearchResultMapper implements SearchResultMapper {

    @Override
    public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
        List<Article> chunk = new ArrayList<>();
        for (SearchHit searchHit : response.getHits()) {
            if (response.getHits().getHits().length <= 0) {
                return null;
            }
            /*SampleEntity user = new SampleEntity();
            user.setId(searchHit.getId());
            user.setMessage((String) searchHit.getSource().get("message"));
            user.setHighlightedMessage(searchHit.getHighlightFields().get("message").fragments()[0].toString());*/
            chunk.add(Article.builder()
                             .authorName(searchHit.getSourceAsMap().get("authorName").toString())
                             .authorSurname(searchHit.getSourceAsMap().get("authorSurname").toString())
                             .content(extractContentHighlight(searchHit))
                             .build());
        }
        if (chunk.size() > 0) {
            return new AggregatedPageImpl<>((List<T>) chunk);
        }
        return null;
    }

    private String extractContentHighlight(SearchHit searchHit) {
        StringBuilder sb = new StringBuilder();
        for (Text t : searchHit.getHighlightFields().get("content").fragments()) {
            sb.append(t.toString());
        }
        return sb.toString();
    }

    @Override
    public <T> T mapSearchHit(SearchHit searchHit, Class<T> type) {
        return null;
    }
}
