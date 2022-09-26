package pl.ekookna.magazynapp.warehouse.repository.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import pl.ekookna.magazynapp.admin.repository.entity.Users;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
public class Warehouse {
    @Id
    @SequenceGenerator(name = "seq_warehouse_id",
            sequenceName = "seq_warehouse_id",
            allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "seq_warehouse_id")
    @Column(updatable = false)
    private Long id;

    private String warehouseName;

    @JsonIgnore
    @ManyToMany(mappedBy = "warehouses", cascade = CascadeType.ALL)
    private Set<Users> users = new HashSet<>();
}
