package ua.deti.tqs.backend.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@AllArgsConstructor
public class UserMealServiceImpl implements UserMealService {
    private UserMealRepository userMealRepository;
    private UserRepository userRepository;
    private MealRepository mealRepository;
    private CurrentUser currentUser;

    @Transactional
    public UserMeal createUserMeal(UserMeal userMeal) {
        Long userId = currentUser.getAuthenticatedUserId();
        if (!userId.equals(userMeal.getUser().getId())) {
            return null;
        }

        Meal meal = mealRepository.findByIdWithRestaurantLock(userMeal.getMeal().getId())
                .orElse(null);

        if (meal == null) return null;

        int bookedSeats = userMealRepository.countByMealId(meal.getId());
        if (bookedSeats >= meal.getRestaurant().getSeats()) {
            return null;
        }

        LocalDate mealDate = meal.getDate();
        LocalDate newStart = mealDate;
        LocalDate newEnd = mealDate.plusDays(1);

        List<UserMeal> userMeals = userMealRepository.findAllByUserId(userId).orElse(null);
        int overlappingCount = 0;
        for (UserMeal um : userMeals) {
            LocalDate existingMealDate = um.getMeal().getDate();
            if (existingMealDate.isBefore(newEnd) && existingMealDate.plusDays(30).isAfter(newStart)) {
                overlappingCount++;
            }
        }

        if (overlappingCount > 0) {
            return null;
        }

        User user = userRepository.getReferenceById(userId);
        UserMeal newBooking = new UserMeal();
        newBooking.setUser(user);
        newBooking.setMeal(meal);
        newBooking.setIsCheck(false);

        return userMealRepository.save(newBooking);
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
    public List<UserMeal> getAllUserMealsByRestaurantId(Long restaurantId) {
        if (!currentUser.getAuthenticatedUserRole().equals(UserRole.STAFF)) return null;

        return userMealRepository.findAllByMeal_RestaurantId(restaurantId).orElse(null);
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
