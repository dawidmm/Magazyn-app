package pl.ekookna.magazynapp.warehouse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.ekookna.magazynapp.warehouse.repository.ArticleStockRepository;
import pl.ekookna.magazynapp.warehouse.repository.entity.ArticleStock;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ArticleStockServiceImpl implements ArticleStockService {

    private ArticleStockRepository articleStockRepository;

    @Override
    public void save(ArticleStock articleStock) {
        articleStockRepository.save(articleStock);
    }

    @Override
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
