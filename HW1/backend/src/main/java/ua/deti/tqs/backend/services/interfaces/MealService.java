package ua.deti.tqs.backend.services.interfaces;

import ua.deti.tqs.backend.entities.Meal;

import java.util.List;

public interface MealService {

    Meal createMeal(Meal meal);

    Meal getMealById(Long id);

    List<Meal> getAllMealsByRestaurantId(Long restaurantId);

    Meal updateMeal(Meal meal);

    boolean deleteMealById(Long id);

}
