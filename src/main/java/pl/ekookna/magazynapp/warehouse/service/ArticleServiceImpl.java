package pl.ekookna.magazynapp.warehouse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ekookna.magazynapp.dto.ArticleDto;
import pl.ekookna.magazynapp.warehouse.exception.ArticleExistException;
import pl.ekookna.magazynapp.warehouse.repository.ArticleRepository;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ArticleServiceImpl implements ArticleService {

    private ArticleRepository articleRepository;

    @Override
    public Article getOne(long id) {
        Optional<Article> optArticle = articleRepository.findById(id);

        if (optArticle.isEmpty())
            throw new IllegalArgumentException("Articel with id not exist: " + id);

        return optArticle.get();
    }

    @Override
    @Transactional
    public void save(ArticleDto articleDto) {
        var optArticle = articleRepository.findOneByName(articleDto.getName());

        if (optArticle.isPresent())
            throw new ArticleExistException("Article with name exist: " + articleDto.getName());

        articleRepository.save(articleDto.toArticle());
    }

    @Override
    public List<Article> findAll() {
        return articleRepository.findAll();
    }
}
