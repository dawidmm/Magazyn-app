package pl.ekookna.magazynapp.warehouse.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import pl.ekookna.magazynapp.admin.exception.UserExistException;
import pl.ekookna.magazynapp.admin.service.SecurityUserDetailsService;
import pl.ekookna.magazynapp.dto.UserDto;
import pl.ekookna.magazynapp.dto.WarehouseDto;
import pl.ekookna.magazynapp.warehouse.service.WarehouseService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@org.springframework.web.bind.annotation.RestController
@AllArgsConstructor
public class RestController {

    private SecurityUserDetailsService userDetailsService;
    private WarehouseService warehouseService;

    @PostMapping(value = "/user", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> saveUser(@Valid @NotNull UserDto userDto) {
        try {
            userDetailsService.createUser(userDto);

            return new ResponseEntity<>("CREATED", HttpStatus.CREATED);
        } catch (UserExistException e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(value = "/warehouse", consumes = "application/x-www-form-urlencoded")
    public ResponseEntity<String> saveWarehouse(@Valid @NotNull WarehouseDto warehouseDto) {
        try {
            warehouseService.save(warehouseDto);

            return new ResponseEntity<>("CREATED", HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
