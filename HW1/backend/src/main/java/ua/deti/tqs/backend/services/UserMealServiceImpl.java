package ua.deti.tqs.backend.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.deti.tqs.backend.authentication.utils.CurrentUser;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.UserMeal;
import ua.deti.tqs.backend.entities.utils.UserRole;
import ua.deti.tqs.backend.repositories.MealRepository;
import ua.deti.tqs.backend.repositories.UserMealRepository;
import ua.deti.tqs.backend.repositories.UserRepository;
import ua.deti.tqs.backend.services.interfaces.UserMealService;

import java.util.List;

@Service
@AllArgsConstructor
public class UserMealServiceImpl implements UserMealService {
    private UserMealRepository userMealRepository;
    private UserRepository userRepository;
    private MealRepository mealRepository;
    private CurrentUser currentUser;

    @Override
    public UserMeal createUserMeal(UserMeal userMeal) {
        if (!currentUser.getAuthenticatedUserId().equals(userMeal.getUser().getId())) return null;

        User user = userRepository.findById(userMeal.getUser().getId()).orElse(null);
        if (user == null) return null;

        Meal meal = mealRepository.findById(userMeal.getMeal().getId()).orElse(null);
        if (meal == null) return null;

        userMeal.setId(null);
        userMeal.setCode(null);
        userMeal.setUser(user);
        userMeal.setMeal(meal);
        return userMealRepository.save(userMeal);
    }

    @Override
    public UserMeal getUserMealById(Long id) {
        UserMeal userMeal = userMealRepository.findById(id).orElse(null);
        if (userMeal == null) return null;

        if (!currentUser.getAuthenticatedUserId().equals(userMeal.getUser().getId())) return null;

        return userMeal;
    }

    @Override
    public List<UserMeal> getAllUserMealsByUserId(Long id){
        if (!currentUser.getAuthenticatedUserId().equals(id)) return null;

        return userMealRepository.findAllByUserId(id).orElse(null);
    }

    @Override
    public UserMeal updateUserMeal(UserMeal userMeal) {
        if (!currentUser.getAuthenticatedUserRole().equals(UserRole.STAFF)) return null;

        UserMeal existingUserMeal = userMealRepository.findById(userMeal.getId()).orElse(null);
        if (existingUserMeal == null) return null;

        int changedFields = 0;
        if (userMeal.getIsCheck() != null && currentUser.getAuthenticatedUserRole().equals(UserRole.STAFF)) {
            existingUserMeal.setIsCheck(userMeal.getIsCheck());
            changedFields++;
        }

        if (changedFields == 0) return null;

        return userMealRepository.save(existingUserMeal);

    }

    @Override
    public boolean deleteUserMealById(Long id) {
        UserMeal userMeal = userMealRepository.findById(id).orElse(null);
        if (userMeal == null) return false;

        if (!currentUser.getAuthenticatedUserId().equals(userMeal.getUser().getId())) return false;

        userMealRepository.deleteById(id);
        return userMealRepository.findById(id).isPresent();
    }
}
