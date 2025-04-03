package ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.repositories.MealRepository;
import ua.deti.tqs.backend.repositories.RestaurantRepository;
import ua.deti.tqs.backend.services.interfaces.MealService;
import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class MealServiceImpl implements MealService {
    private MealRepository mealRepository;
    private RestaurantRepository restaurantRepository;

    @Override
    public Meal createMeal(Meal meal) {
        if (meal.getDate() == null
                || meal.getMeal() == null || meal.getMeal().trim().isEmpty()
                || meal.getPrice() == null || meal.getPrice().compareTo(BigDecimal.ZERO) <= 0
                || meal.getRestaurant() == null) return null;

        Restaurant restaurant = restaurantRepository.findById(meal.getRestaurant().getId()).orElse(null);
        if (restaurant == null) return null;

        Meal existingMeal = mealRepository.findMealByMealAndRestaurantAndDate(meal.getMeal(), restaurant, meal.getDate()).orElse(null);
        if (existingMeal != null) return null;

        meal.setRestaurant(restaurant);
        meal.setId(null);

        return mealRepository.save(meal);
    }


    @Override
    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElse(null);
    }

    @Override
    public List<Meal> getAllMealsByRestaurantId(Long restaurantId) {
        return mealRepository.findAllByRestaurantId(restaurantId).orElse(null);
    }

    @Override
    public Meal updateMeal(Meal meal) {
        Long id = meal.getId();
        Meal existingMeal = mealRepository.findById(id).orElse(null);

        if (existingMeal == null) return null;

        int changedFields = 0;
        if (meal.getMeal() != null && !meal.getMeal().trim().isEmpty()) {
            existingMeal.setMeal(meal.getMeal());
            changedFields++;
        }

        if (meal.getPrice() != null && meal.getPrice().compareTo(BigDecimal.ZERO) > 0) {
            existingMeal.setPrice(meal.getPrice());
            changedFields++;
        }

        if (meal.getDate() != null) {
            existingMeal.setDate(meal.getDate());
            changedFields++;
        }

        if (changedFields == 0) return null;

        return mealRepository.save(existingMeal);

    }

    @Override
    public boolean deleteMealById(Long id) {
        mealRepository.deleteById(id);
        return mealRepository.findById(id).isEmpty();
    }
}
