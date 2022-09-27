package pl.ekookna.magazynapp.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Optional<Article> findOneByName(String name);
}
