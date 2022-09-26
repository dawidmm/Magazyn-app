package pl.ekookna.magazynapp.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class ArticleDto {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String type;

    public Article toArticle() {
        Article article = new Article();
        article.setName(name);
        article.setType(type);

        return article;
    }
}
