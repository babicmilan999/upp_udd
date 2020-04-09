package upp_udd.project.services;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import upp_udd.project.dto.ArticleDetailsDto;
import upp_udd.project.model.Article;
import upp_udd.project.repositories.ArticleRepository;

@RequiredArgsConstructor
@Service
public class ArticleService {

    @Value("${upload.directory}")
    private String uploadDirectory;

    private final ArticleRepository articleRepository;

    public Article uploadAndSaveArticle(MultipartFile multipartFile, ArticleDetailsDto articleDetailsDto) {
        final String filePath = upload(multipartFile);
        return articleRepository.save(mapToArticle(articleDetailsDto, filePath));
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
                      .name(articleDetailsDto.getName())
                      .scientificField(articleDetailsDto.getScientificField())
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
