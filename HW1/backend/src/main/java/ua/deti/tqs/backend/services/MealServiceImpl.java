package ua.deti.tqs.backend.services;

import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.repositories.MealRepository;
import ua.deti.tqs.backend.services.interfaces.MealService;

import java.util.List;

public class MealServiceImpl implements MealService {
    private MealRepository mealRepository;

    @Override
    public Meal createMeal(Meal meal) {
        return null;
    }

    @Override
    public Meal getMealById(Long id) {
        return null;
    }

    @Override
    public List<Meal> getAllMealsByRestaurantId(Long restaurantId) {
        return List.of();
    }

    @Override
    public Meal updateMeal(Meal meal) {
        return null;
    }

    @Override
    public void deleteMealById(Long id) {

    }
}
