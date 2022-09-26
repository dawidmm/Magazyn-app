package pl.ekookna.magazynapp.warehouse.service;

import org.springframework.web.multipart.MultipartFile;
import pl.ekookna.magazynapp.warehouse.repository.entity.ArticleStock;

import java.util.List;

public interface ArticleStockService {
    void save(ArticleStock articleStock);

    List<String> storeFiles(List<MultipartFile> file, long id);
}
