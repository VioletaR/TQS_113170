package ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.deti.tqs.backend.entities.UserMeal;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserMealRepository extends JpaRepository<UserMeal, Long> {
    Optional<List<UserMeal>> findAllByUserId(Long userId);

    Optional<List<UserMeal>> findAllByMeal_RestaurantId(Long restaurantId);


    @Query("SELECT COUNT(um) FROM UserMeal um " +
            "JOIN um.meal m " +
            "WHERE um.user.id = :userId " +
            "AND m.date < :newEnd " +
            "AND FUNCTION('DATE_ADD', m.date, 30) > :newStart")
    int  countOverlappingMeals(
            @Param("userId") Long userId,
            @Param("newStart") LocalDate newStart,
            @Param("newEnd") LocalDate newEnd
    );

    @Query("SELECT COUNT(um) FROM UserMeal um WHERE um.meal.id = :mealId")
    int countByMealId(@Param("mealId") Long mealId);
}
