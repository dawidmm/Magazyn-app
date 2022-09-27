package pl.ekookna.magazynapp.admin.service;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ekookna.magazynapp.admin.exception.UserExistException;
import pl.ekookna.magazynapp.admin.repository.UsersRepository;
import pl.ekookna.magazynapp.admin.repository.entity.Users;
import pl.ekookna.magazynapp.dto.UserDto;
import pl.ekookna.magazynapp.warehouse.exception.WarehouseNotFoundException;
import pl.ekookna.magazynapp.warehouse.repository.WarehouseRepository;

import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private UsersRepository usersRepository;
    private PasswordEncoder encoder;
    private WarehouseRepository warehouseRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findUserByLogin(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not present");

        return user.get();
    }

    public void createUser(UserDetails user) throws UserExistException {
        UserDetails userFromDb = null;

        try {
            userFromDb = loadUserByUsername(user.getUsername());
        } catch (UsernameNotFoundException e) {
            ((Users) user).setPassword(encoder.encode(user.getPassword()));
            usersRepository.save((Users) user);
        }
        if (userFromDb != null)
            throw new UserExistException("Username already exist");
    }

    public void createUser(UserDto userDto) {
        UserDetails user = userDto.toUserDetails();
        var optWarehouse = warehouseRepository.findOneByWarehouseName(userDto.getWarehouse());

        if (userDto.getWarehouse() != null && !userDto.getWarehouse().isBlank()) {
            if (optWarehouse.isPresent()) {
                ((Users) user).setWarehouses(Set.of(optWarehouse.get()));
            } else {
                throw new WarehouseNotFoundException("Warehouse not found: " + userDto.getWarehouse());
            }
        }

        createUser(user);
    }

    //FIXME: ONLY FOR DEVELOPMENT AND TEST
    @Bean
    private void defaultUsers() {
        var usersList = usersRepository.findAll();

        if (usersList.isEmpty()) {
            Users userAdmin = new Users();
            userAdmin.setId(1L);
            userAdmin.setActive(true);
            userAdmin.setLogin("admin");
            userAdmin.setPassword("admin");
            userAdmin.setRole("ADMIN");

            Users user = new Users();
            user.setId(2L);
            user.setActive(true);
            user.setLogin("user");
            user.setPassword("user");
            user.setRole("USER");

            createUser(user);
            createUser(userAdmin);
        }
    }
}
