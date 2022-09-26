package pl.ekookna.magazynapp.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ekookna.magazynapp.warehouse.repository.entity.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
