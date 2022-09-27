package pl.ekookna.magazynapp.warehouse.service;

import pl.ekookna.magazynapp.admin.repository.entity.Users;
import pl.ekookna.magazynapp.dto.WarehouseDto;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseService {
    List<Warehouse> findAll();

    Optional<Warehouse> findOneByName(String warehouse);

    Optional<Warehouse> findOneById(long id);

    Warehouse getOneById(long id);

    Warehouse save(WarehouseDto warehouse);

    List<Warehouse> findAllForUser(Users user);
}
