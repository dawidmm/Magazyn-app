package pl.ekookna.magazynapp.admin.security.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.ekookna.magazynapp.admin.repository.UsersRepository;
import pl.ekookna.magazynapp.admin.repository.entity.Users;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private UsersRepository usersRepository;
    private PasswordEncoder encoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Users> user = usersRepository.findUserByLogin(username);

        if (user.isEmpty())
            throw new UsernameNotFoundException("User not present");
        return user.get();
    }

    public void createUser(UserDetails user) {
        usersRepository.save((Users) user);
    }

    //FIXME: ONLY FOR DEVELOPMENT AND TEST
    @Bean
    private List<Users> defaultUsers() {
        var usersList = usersRepository.findAll();

        if (usersList.isEmpty()) {
            Users userAdmin = new Users();
            userAdmin.setId(1L);
            userAdmin.setActive(true);
            userAdmin.setLogin("admin");
            userAdmin.setPassword(encoder.encode("admin"));
            userAdmin.setRole("ADMIN");

            Users user = new Users();
            user.setId(2L);
            user.setActive(true);
            user.setLogin("user");
            user.setPassword(encoder.encode("user"));
            user.setRole("USER");

            createUser(user);
            createUser(userAdmin);

            usersList = usersRepository.findAll();
        }

        return usersList;
    }
}
