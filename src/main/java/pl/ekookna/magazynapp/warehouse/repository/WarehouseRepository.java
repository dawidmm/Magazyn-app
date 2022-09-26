package pl.ekookna.magazynapp.warehouse.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;

import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {
    Optional<Warehouse> findOneByWarehouseName(String warehouse);
}
