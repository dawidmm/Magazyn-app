package pl.ekookna.magazynapp.warehouse.service;

import pl.ekookna.magazynapp.dto.WarehouseDto;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    List<Warehouse> findAll();

    Optional<Warehouse> findOneByName(String warehouse);

    Warehouse save(WarehouseDto warehouse);
}
