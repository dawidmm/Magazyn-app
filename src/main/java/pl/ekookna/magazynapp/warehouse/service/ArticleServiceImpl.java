package pl.ekookna.magazynapp.warehouse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ekookna.magazynapp.dto.ArticleDto;
import pl.ekookna.magazynapp.warehouse.repository.ArticleRepository;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;

import java.util.List;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;

    @Override
    public void save(ArticleDto articleDto) {
        articleRepository.save(articleDto.toArticle());
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
