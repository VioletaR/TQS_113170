package ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal, Long> {
    Optional<List<Meal>> findAllByRestaurantId(Long restaurantId);

    Optional<Meal> findMealByMealAndRestaurantAndDate(String meal, Restaurant restaurant, LocalDate date);
}
