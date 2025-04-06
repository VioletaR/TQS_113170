package ua.deti.tqs.backend.repositories;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    Optional<List<Meal>> findAllByRestaurantId(Long restaurantId);

    Optional<Meal> findMealByMealAndRestaurantAndDate(String meal, Restaurant restaurant, LocalDateTime date);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT m FROM Meal m JOIN FETCH m.restaurant WHERE m.id = :mealId")
    Optional<Meal> findByIdWithRestaurantLock(@Param("mealId") Long mealId);
}
