package upp_udd.project.api;

import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import upp_udd.project.dto.ArticleDetailsDto;
import upp_udd.project.dto.QueryDto;
import upp_udd.project.model.Article;
import upp_udd.project.services.ArticleService;

@RestController
@AllArgsConstructor
@RequestMapping("/articles")
@CrossOrigin(origins = "http://localhost:4200")
public class ArticleController {

    private ArticleService articleService;

    @PostMapping("/upload")
    public Article uploadFile(@RequestParam("file") MultipartFile file, ArticleDetailsDto articleDetailsDto) {
        return articleService.uploadAndSaveArticle(file, articleDetailsDto);
    }

    @GetMapping("/search")
    public Page<Article> search(QueryDto queryDto) {
        return articleService.search(queryDto);
    }

}
