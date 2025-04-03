package ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.backend.entities.UserMeal;

import java.util.List;
import java.util.Optional;

public interface UserMealRepository extends JpaRepository<UserMeal, Long> {
    Optional<List<UserMeal>> findAllByUserId(Long userId);
}
