package pl.ekookna.magazynapp.warehouse.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import pl.ekookna.magazynapp.utils.StringListConverter;

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

    @ManyToOne(targetEntity = Article.class)
    @JoinColumn(name = "article_id", referencedColumnName = "id")
    private Article article;

    @JsonIgnore
    @ManyToMany()
    @JoinTable(
            name = "article_stock_warehouses_ids",
            joinColumns = {@JoinColumn(name = "article_stock_id")},
            inverseJoinColumns = {@JoinColumn(name = "warehouse_id")}
    )
    private List<Warehouse> warehouse = new ArrayList<>();

    private Long amount;

    private int vat;

    private int price;

    @Convert(converter = StringListConverter.class)
    @Column(name = "files", columnDefinition = "text")
    private List<String> files;
}
