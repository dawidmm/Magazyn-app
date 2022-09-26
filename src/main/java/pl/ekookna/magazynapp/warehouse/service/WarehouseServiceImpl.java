package pl.ekookna.magazynapp.warehouse.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.ekookna.magazynapp.admin.repository.UsersRepository;
import pl.ekookna.magazynapp.admin.repository.entity.Users;
import pl.ekookna.magazynapp.dto.WarehouseDto;
import pl.ekookna.magazynapp.warehouse.repository.WarehouseRepository;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class WarehouseServiceImpl implements WarehouseService {

    private WarehouseRepository warehouseRepository;
    private UsersRepository usersRepository;

    @Override
    public List<Warehouse> findAll() {
        return warehouseRepository.findAll();
    }

    @Override
    public Optional<Warehouse> findOneByName(String warehouse) {
        return warehouseRepository.findOneByWarehouseName(warehouse);
    }

    @Override
    @Transactional
    public Warehouse save(WarehouseDto warehouseDto) {
        var optUser = usersRepository.findUserByLogin(warehouseDto.getUser());
        Warehouse warehouse = warehouseDto.toWarehouse();

        warehouse = warehouseRepository.save(warehouse);

        if (optUser.isPresent()) {
            Users user = optUser.get();
            Set<Warehouse> set = new HashSet<>(user.getWarehouses());
            set.add(warehouse);

            user.setWarehouses(set);
        } else {
            throw new IllegalArgumentException("User is not present: " + warehouseDto.getUser());
        }

        return warehouse;
    }
}
