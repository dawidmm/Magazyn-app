package pl.ekookna.magazynapp.warehouse.service;

import pl.ekookna.magazynapp.dto.ArticleDto;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;

import java.util.List;

public interface ArticleService {

    void save(ArticleDto articleDto);

    Article getOne(long id);

    List<Article> findAll();
}
