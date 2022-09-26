package pl.ekookna.magazynapp.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.ekookna.magazynapp.warehouse.repository.entity.ArticleStock;

@Repository
public interface ArticleStockRepository extends JpaRepository<ArticleStock, Long> {
}
