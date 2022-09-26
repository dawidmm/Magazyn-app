package pl.ekookna.magazynapp.dto;

import lombok.Data;
import org.springframework.validation.annotation.Validated;
import pl.ekookna.magazynapp.warehouse.repository.entity.Warehouse;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Validated
public class WarehouseDto {

    @NotNull
    @NotBlank
    private String name;

    @NotNull
    @NotBlank
    private String user;

    public Warehouse toWarehouse() {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseName(name);

        return warehouse;
    }

}
