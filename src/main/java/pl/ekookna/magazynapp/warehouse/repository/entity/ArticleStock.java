package pl.ekookna.magazynapp.warehouse.repository.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "article_stock")
public class ArticleStock {

    @Id
    @SequenceGenerator(name = "article_stock_id_seq",
            sequenceName = "article_stock_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "article_stock_id_seq")
    private Long id;

    private Long articleId;

    private Long amount;

    private int vat;

    private int price;

    @Type(type = "jsonb")
    private List<String> files = new ArrayList<>();
}
