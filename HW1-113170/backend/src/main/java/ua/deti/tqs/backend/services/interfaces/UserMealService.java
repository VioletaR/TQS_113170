package ua.deti.tqs.backend.services.interfaces;

import ua.deti.tqs.backend.entities.UserMeal;

import java.util.List;

public interface UserMealService {
    UserMeal createUserMeal(UserMeal userMeal);

    UserMeal getUserMealById(Long id);

    List<UserMeal> getAllUserMealsByUserId(Long userId);

    UserMeal updateUserMeal(UserMeal userMeal);

    boolean deleteUserMealById(Long id);

    List<UserMeal> getAllUserMealsByRestaurantId(Long restaurantId);
}
