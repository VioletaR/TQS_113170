package ua.deti.tqs.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import ua.deti.tqs.backend.authentication.utils.CurrentUser;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.utils.UserRole;
import ua.deti.tqs.backend.entities.UserMeal;
import ua.deti.tqs.backend.repositories.MealRepository;
import ua.deti.tqs.backend.repositories.UserMealRepository;
import ua.deti.tqs.backend.repositories.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserMealServiceTest {

    @Mock
    private UserMealRepository userMealRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MealRepository mealRepository;

    @Mock
    private CurrentUser currentUser;

    @InjectMocks
    private UserMealServiceImpl userMealService;

    private User user1;
    private Meal meal1;
    private Restaurant restaurant1;
    private UserMeal userMeal1;

    @BeforeEach
    void setUp() {
        user1 = new User();
        user1.setId(1L);
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(UserRole.USER);

        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("restaurant1");
        restaurant1.setLocation("location1");
        restaurant1.setSeats(10);

        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setMeal("meal1");
        meal1.setRestaurant(restaurant1);
        meal1.setPrice(BigDecimal.valueOf(10));
        meal1.setDate(LocalDateTime.now());

        userMeal1 = new UserMeal();
        userMeal1.setId(1L);
        userMeal1.setUser(user1);
        userMeal1.setMeal(meal1);
        userMeal1.setIsCheck(false);

    }

    @Test
    void createUserMeal_UserIdsDontMatch_ReturnsNull() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(2L);
        assertNull(userMealService.createUserMeal(userMeal1));
    }

    @Test
    void createUserMeal_MealNotFound_ReturnsNull() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(user1.getId());
        when(mealRepository.findByIdWithRestaurantLock(userMeal1.getMeal().getId())).thenReturn(Optional.empty());
        assertNull(userMealService.createUserMeal(userMeal1));
    }

    @Test
    void createUserMeal_NoSeatsAvailable_ReturnsNull() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(user1.getId());
        when(mealRepository.findByIdWithRestaurantLock(userMeal1.getMeal().getId())).thenReturn(Optional.of(meal1));
        when(userMealRepository.countByMealId(userMeal1.getMeal().getId())).thenReturn(10);
        assertNull(userMealService.createUserMeal(userMeal1));
    }

    @Test
    void createUserMeal_OverlappingBookings_Plus_ReturnsNull() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(user1.getId());
        when(mealRepository.findByIdWithRestaurantLock(userMeal1.getMeal().getId())).thenReturn(Optional.of(meal1));
        when(userMealRepository.countByMealId(userMeal1.getMeal().getId())).thenReturn(1);

        UserMeal overlappingMeal = new UserMeal();
        Meal overlappingMealEntity = new Meal();
        overlappingMealEntity.setDate(meal1.getDate().plusMinutes(30));
        overlappingMeal.setMeal(overlappingMealEntity);

        UserMeal overlappingMeal1 = new UserMeal();
        Meal overlappingMealEntity1 = new Meal();
        overlappingMealEntity1.setDate(meal1.getDate().minusHours(2));
        overlappingMeal1.setMeal(overlappingMealEntity1);

        UserMeal overlappingMeal2 = new UserMeal();
        Meal overlappingMealEntity2 = new Meal();
        overlappingMealEntity2.setDate(meal1.getDate().plusHours(2));
        overlappingMeal2.setMeal(overlappingMealEntity2);

        when(userMealRepository.findAllByUserId(anyLong())).thenReturn(Optional.of(List.of(overlappingMeal,overlappingMeal1, overlappingMeal2)));
        assertNull(userMealService.createUserMeal(userMeal1));
    }

    @Test
    void createUserMeal_OverlappingBookings_Minus_ReturnsNull() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(user1.getId());
        when(mealRepository.findByIdWithRestaurantLock(userMeal1.getMeal().getId())).thenReturn(Optional.of(meal1));
        when(userMealRepository.countByMealId(userMeal1.getMeal().getId())).thenReturn(1);

        UserMeal overlappingMeal = new UserMeal();
        Meal overlappingMealEntity = new Meal();
        overlappingMealEntity.setDate(meal1.getDate().minusMinutes(30));
        overlappingMeal.setMeal(overlappingMealEntity);

        when(userMealRepository.findAllByUserId(anyLong())).thenReturn(Optional.of(List.of(overlappingMeal)));
        assertNull(userMealService.createUserMeal(userMeal1));
    }

    @Test
    void createUserMeal_Success_ReturnsUserMeal() {
        userMeal1.setIsCheck(true);
        userMeal1.setCode("abc");

        when(currentUser.getAuthenticatedUserId()).thenReturn(user1.getId());
        when(mealRepository.findByIdWithRestaurantLock(userMeal1.getMeal().getId())).thenReturn(Optional.of(meal1));
        when(userMealRepository.countByMealId(userMeal1.getMeal().getId())).thenReturn(5);
        when(userRepository.getReferenceById(userMeal1.getUser().getId())).thenReturn(user1);
        when(userMealRepository.save(any(UserMeal.class))).thenReturn(userMeal1);

        UserMeal result = userMealService.createUserMeal(userMeal1);
        assertNotNull(result);
        assertEquals(userMeal1.getId(), result.getId());

    }

    @Test
    void getUserMealById_ExistsAndUserIsOwner_ReturnsUserMeal() {
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.of(userMeal1));
        when(currentUser.getAuthenticatedUserId()).thenReturn(user1.getId());
        assertEquals(userMeal1, userMealService.getUserMealById(userMeal1.getId()));
    }

    @Test
    void getUserMealById_NotExists_ReturnsNull() {
        when(userMealRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(userMealService.getUserMealById(userMeal1.getId()));
    }

    @Test
    void getUserMealById_UserNotOwner_ReturnsNull() {
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.of(userMeal1));
        when(currentUser.getAuthenticatedUserId()).thenReturn(2L);
        assertNull(userMealService.getUserMealById(userMeal1.getId()));
    }

    @Test
    void getUserMealById_NoAuthenticatedUser_ReturnsNull() {
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.of(userMeal1));
        when(currentUser.getAuthenticatedUserId()).thenReturn(null);
        assertNull(userMealService.getUserMealById(userMeal1.getId()));
    }

    @Test
    void getAllUserMealsByUserId_UserIsOwner_ReturnsList() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(user1.getId());
        when(userMealRepository.findAllByUserId(user1.getId())).thenReturn(Optional.of(List.of(userMeal1)));
        assertEquals(List.of(userMeal1), userMealService.getAllUserMealsByUserId(userMeal1.getId()));
    }

    @Test
    void getAllUserMealsByUserId_UserNotOwner_ReturnsNull() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(2L);
        assertNull(userMealService.getAllUserMealsByUserId(user1.getId()));
    }

    @Test
    void getAllUserMealsByUserId_NoAuthenticatedUser_ReturnsNull() {
        when(currentUser.getAuthenticatedUserId()).thenReturn(null);
        assertNull(userMealService.getAllUserMealsByUserId(user1.getId()));
    }

    @Test
    void getAllUserMealsByRestaurantId_UserIsStaff_ReturnsList() {
        when(currentUser.getAuthenticatedUserRole()).thenReturn(UserRole.STAFF);
        when(userMealRepository.findAllByMeal_RestaurantId(restaurant1.getId())).thenReturn(Optional.of(List.of(userMeal1)));
        assertEquals(List.of(userMeal1), userMealService.getAllUserMealsByRestaurantId(restaurant1.getId()));
    }

    @Test
    void getAllUserMealsByRestaurantId_UserNotStaff_ReturnsNull() {
        when(currentUser.getAuthenticatedUserRole()).thenReturn(UserRole.USER);
        assertNull(userMealService.getAllUserMealsByRestaurantId(restaurant1.getId()));
    }

    @Test
    void getAllUserMealsByRestaurantId_NoAuthenticatedUser_ReturnsNull() {
        when(currentUser.getAuthenticatedUserRole()).thenReturn(null);
        assertNull(userMealService.getAllUserMealsByRestaurantId(restaurant1.getId()));
    }

    @Test
    void updateUserMeal_UserNotStaff_ReturnsNull() {
        when(currentUser.getAuthenticatedUserRole()).thenReturn(UserRole.USER);
        assertNull(userMealService.updateUserMeal(userMeal1));
    }

    @Test
    void updateUserMeal_NoAuthenticatedUser_ReturnsNull() {
        when(currentUser.getAuthenticatedUserRole()).thenReturn(null);
        assertNull(userMealService.updateUserMeal(userMeal1));
    }

    @Test
    void updateUserMeal_CheckIsNull_ReturnsNull() {
        when(currentUser.getAuthenticatedUserRole()).thenReturn(UserRole.STAFF);
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.of(userMeal1));
        userMeal1.setIsCheck(null);
        assertNull(userMealService.updateUserMeal(userMeal1));
    }

    @Test
    void updateUserMeal_UserIsStaff_UpdatesIsCheck() {
        when(currentUser.getAuthenticatedUserRole()).thenReturn(UserRole.STAFF);
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.of(userMeal1));

        UserMeal updatedUserMeal = new UserMeal();
        updatedUserMeal.setId(1L);
        updatedUserMeal.setIsCheck(true);

        when(userMealRepository.save(any(UserMeal.class))).thenReturn(userMeal1);
        UserMeal result = userMealService.updateUserMeal(updatedUserMeal);

        assertNotNull(result);
        assertTrue(result.getIsCheck());
    }


    @Test
    void updateUserMeal_UserMealNotFound_ReturnsNull() {
        when(currentUser.getAuthenticatedUserRole()).thenReturn(UserRole.STAFF);
        when(userMealRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertNull(userMealService.updateUserMeal(userMeal1));
    }



    @Test
    void deleteUserMealById_UserNotOwner_ReturnsFalse() {
        when(userMealRepository.existsById(userMeal1.getId())).thenReturn(true);
        when(currentUser.getAuthenticatedUserId()).thenReturn(2L);
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.of(userMeal1));

        assertFalse(userMealService.deleteUserMealById(userMeal1.getId()));
    }

    @Test
    void deleteUserMealById_NoUserAuthenticated_ReturnsFalse() {
        when(userMealRepository.existsById(userMeal1.getId())).thenReturn(true);
        when(currentUser.getAuthenticatedUserId()).thenReturn(null);
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.of(userMeal1));

        assertFalse(userMealService.deleteUserMealById(userMeal1.getId()));
    }


    @Test
    void deleteUserMealById_UserMealNotFound_ReturnsFalse() {
        when(userMealRepository.existsById(userMeal1.getId())).thenReturn(true);
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.empty());

        assertFalse(userMealService.deleteUserMealById(userMeal1.getId()));
    }

    @Test
    void deleteUserMealById_Success_ReturnsTrue() {
        when(userMealRepository.existsById(userMeal1.getId())).thenReturn(true);
        when(userMealRepository.findById(userMeal1.getId())).thenReturn(Optional.of(userMeal1));
        when(currentUser.getAuthenticatedUserId()).thenReturn(user1.getId());


        boolean result = userMealService.deleteUserMealById(userMeal1.getId());
        assertThat(result).isTrue();
    }


    @Test
    void deleteUserMealById_MealNotFound_ReturnsFalse() {
        when(userMealRepository.existsById(userMeal1.getId())).thenReturn(false);

        assertFalse(userMealService.deleteUserMealById(userMeal1.getId()));
    }
}

