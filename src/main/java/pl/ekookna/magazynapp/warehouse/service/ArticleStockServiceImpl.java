package pl.ekookna.magazynapp.warehouse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ekookna.magazynapp.admin.repository.entity.Users;
import pl.ekookna.magazynapp.dto.ArticleStockDto;
import pl.ekookna.magazynapp.warehouse.exception.AmountTooLargeException;
import pl.ekookna.magazynapp.warehouse.exception.ArticleStockNotFoundException;
import pl.ekookna.magazynapp.warehouse.repository.ArticleStockRepository;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;
import pl.ekookna.magazynapp.warehouse.repository.entity.ArticleStock;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@AllArgsConstructor
public class ArticleStockServiceImpl implements ArticleStockService {

    private ArticleStockRepository articleStockRepository;
    private WarehouseService warehouseService;

    @Override
    @Transactional
    public void save(ArticleStock articleStock) {
        articleStockRepository.save(articleStock);
    }

    @Override
    public ArticleStock getOne(long id) {
        Optional<ArticleStock> optArticleStock = articleStockRepository.findById(id);

        if (optArticleStock.isEmpty())
            throw new ArticleStockNotFoundException("Article Stock with id not found: " + id);

        return optArticleStock.get();
    }

    @Override
    public List<ArticleStock> findAll() {
        return articleStockRepository.findAll();
    }

    @Override
    @Transactional
    public void removeArticleStockAmount(ArticleStockDto articleDto) {
        ArticleStock articleStock = getOne(articleDto.getId());

        long currentAmount = articleStock.getAmount();
        long amount = articleDto.getAmount();

        if (currentAmount < amount)
            throw new AmountTooLargeException("Amount is too large, expected maximum value: " + currentAmount);

        articleStock.setAmount(currentAmount - amount);

        if (articleStock.getAmount() == 0)
            articleStockRepository.delete(articleStock);
        else
            articleStockRepository.saveAndFlush(articleStock);
    }

    @Override
    @Transactional
    public List<String> storeFiles(List<MultipartFile> files, long articleId) {
        List<String> filesName = new ArrayList<>();
        String directoryPath = "files/" + articleId;
        Path path = Paths.get(directoryPath);

        if (!Files.exists(path)) {
            try {
                Files.createDirectory(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        for (MultipartFile f : files) {
            if (f.getOriginalFilename() != null && !f.getOriginalFilename().isBlank() &&
                    (f.getOriginalFilename().contains(".pdf") || f.getOriginalFilename().contains(".xml"))) {
                storeFile(f, directoryPath);
                filesName.add(f.getOriginalFilename());
            }
        }

        return filesName;
    }

    @Override
    public List<Article> getArticleFromArticleStock() {
        List<Article> articleList = new ArrayList<>();
        List<ArticleStock> articleStockList = findAll();

        for (ArticleStock as : articleStockList) {
            articleList.add(as.getArticle());
        }

        return articleList;
    }

    @Override
    public Set<ArticleStock> findAllForUser(Users user) {
        List<Warehouse> warehouseList = warehouseService.findAllForUser(user);
        Set<ArticleStock> articleStockList = new HashSet<>();

        for (Warehouse w : warehouseList) {
            articleStockList.addAll(w.getArticleStocks());
        }

        return articleStockList;
    }

    private void storeFile(MultipartFile file, String path) {
        try {
            Files.copy(file.getInputStream(), Paths.get(path).resolve(file.getOriginalFilename()));
        } catch (FileAlreadyExistsException e) {
            throw new IllegalArgumentException("File already exist: " + file.getOriginalFilename());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
