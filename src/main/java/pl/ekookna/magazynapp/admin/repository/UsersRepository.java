package pl.ekookna.magazynapp.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.ekookna.magazynapp.admin.repository.entity.Users;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Optional<Users> findUserByLogin(String username);
}
