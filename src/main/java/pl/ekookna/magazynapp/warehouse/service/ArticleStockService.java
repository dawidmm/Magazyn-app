package pl.ekookna.magazynapp.warehouse.service;

import org.springframework.web.multipart.MultipartFile;
import pl.ekookna.magazynapp.admin.repository.entity.Users;
import pl.ekookna.magazynapp.dto.ArticleStockDto;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;
import pl.ekookna.magazynapp.warehouse.repository.entity.ArticleStock;

import java.util.List;
import java.util.Set;

public interface ArticleStockService {
    void save(ArticleStock articleStock);

    List<ArticleStock> findAll();

    ArticleStock getOne(long id);

    List<String> storeFiles(List<MultipartFile> file, long id);

    void removeArticleStockAmount(ArticleStockDto articleDto);

    List<Article> getArticleFromArticleStock();

    Set<ArticleStock> findAllForUser(Users user);
}
