package pl.ekookna.magazynapp.warehouse.repository.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Article {

    @Id
    @SequenceGenerator(name = "article_id_seq",
            sequenceName = "article_id_seq",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "article_id_seq")
    @Column(updatable = false)
    private Long id;
    private String name;
    private String type;
}
