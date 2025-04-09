package ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.backend.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);
}
