package upp_udd.project.services;

import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import upp_udd.project.dto.ArticleDetailsDto;
import upp_udd.project.dto.QueryDto;
import upp_udd.project.model.Article;
import upp_udd.project.repositories.ArticleRepository;

@RequiredArgsConstructor
@Service
public class ArticleService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    private final ArticleRepository articleRepository;
    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ArticleSearchResultMapper articleSearchResultMapper;

    public Article uploadAndSaveArticle(MultipartFile multipartFile, ArticleDetailsDto articleDetailsDto) {
        final String filePath = upload(multipartFile);
        return articleRepository.save(mapToArticle(articleDetailsDto, filePath));
    }

    public Page<Article> search(QueryDto queryDto) {
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (!StringUtils.isEmpty(queryDto.getTitle())) {
            addOperator(boolQueryBuilder, "title", queryDto.getTitle(), queryDto.getOperator());
        }
        if (!StringUtils.isEmpty(queryDto.getAuthorName())) {
            addOperator(boolQueryBuilder, "authorName", queryDto.getAuthorName(), queryDto.getOperator());
        }
        if (!StringUtils.isEmpty(queryDto.getAuthorSurname())) {
            addOperator(boolQueryBuilder, "authorSurname", queryDto.getAuthorSurname(), queryDto.getOperator());
        }
        if (!StringUtils.isEmpty(queryDto.getContent())) {
            addOperator(boolQueryBuilder, "content", queryDto.getContent(), queryDto.getOperator());
        }
        if (!StringUtils.isEmpty(queryDto.getKeyWords())) {
            addOperator(boolQueryBuilder, "keyWords", queryDto.getKeyWords(), queryDto.getOperator());
        }
        if (!StringUtils.isEmpty(queryDto.getMagazineName())) {
            addOperator(boolQueryBuilder, "magazineName", queryDto.getMagazineName(), queryDto.getOperator());
        }
        if (!StringUtils.isEmpty(queryDto.getScientificFields())) {
            addOperator(boolQueryBuilder, "scientificFields", queryDto.getScientificFields(), queryDto.getOperator());
        }

        return elasticsearchTemplate.queryForPage(new NativeSearchQueryBuilder().withQuery(boolQueryBuilder)
                                                                                .withHighlightFields(new HighlightBuilder.Field("content"))
                                                                                .build(),
                                                  Article.class,
                                                  articleSearchResultMapper);
    }

    private void addOperator(BoolQueryBuilder boolQueryBuilder, String fieldName, String fieldValue, QueryDto.Operator operator) {

        switch (operator) {
            case AND: { boolQueryBuilder.must(getMatching(fieldName, fieldValue)); return; }
            case OR: boolQueryBuilder.should(getMatching(fieldName, fieldValue));
        }

    }

    private QueryBuilder getMatching(String fieldName, String fieldValue) {
        if (fieldValue.trim().matches("\".*\"")) {
            return matchPhraseQuery(fieldName, fieldValue);
        }
        return matchQuery(fieldName, fieldValue);
    }

    @SneakyThrows
    private String upload(MultipartFile file) {
        String filePath = uploadDirectory.concat(StringUtils.cleanPath(file.getOriginalFilename()));
        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);
        return filePath;
    }

    private Article mapToArticle(ArticleDetailsDto articleDetailsDto, String filePath) {
        return Article.builder()
                      .content(extractContent(filePath))
                      .authorName(articleDetailsDto.getAuthorName())
                      .authorSurname(articleDetailsDto.getAuthorSurname())
                      .keyWords(articleDetailsDto.getKeyWords())
                      .magazineName(articleDetailsDto.getMagazineName())
                      .scientificFields(articleDetailsDto.getScientificFields())
                      .title(articleDetailsDto.getTitle())
                      .filePath(filePath)
                      .build();
    }

    @SneakyThrows
    private String extractContent(String filePath) {
        PDDocument document = PDDocument.load(new File(filePath));
        String content = new PDFTextStripper().getText(document);
        document.close();
        return content;
    }

}
