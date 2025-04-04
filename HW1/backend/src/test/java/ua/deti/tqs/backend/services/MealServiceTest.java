package ua.deti.tqs.backend.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.repositories.MealRepository;
import ua.deti.tqs.backend.repositories.RestaurantRepository;
import ua.deti.tqs.backend.services.MealServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MealServiceTest {

    @Mock
    private MealRepository mealRepository;

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private MealServiceImpl mealService;

    private Restaurant restaurant1;
    private Meal meal1;

    @BeforeEach
    void setUp() {
        restaurant1 = new Restaurant();
        restaurant1.setId(1L);
        restaurant1.setName("restaurant1");
        restaurant1.setDistrict("district1");
        restaurant1.setSeats(10);

        meal1 = new Meal();
        meal1.setId(1L);
        meal1.setMeal("meal1");
        meal1.setDate(LocalDate.now());
        meal1.setPrice(BigDecimal.valueOf(10));
        meal1.setRestaurant(restaurant1);
    }

    @Test
    void whenCreateValidMeal_thenReturnMeal() {
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(restaurant1));
        when(mealRepository.save(any(Meal.class))).thenReturn(meal1);

        Meal created = mealService.createMeal(meal1);

        assertThat(created).isEqualTo(meal1);
        verify(mealRepository).save(meal1);
    }

    @Test
    void whenCreateMealWithInvalidData_thenReturnNull() {
        // Test null meal name
        Meal invalid = new Meal();
        invalid.setMeal(null);
        invalid.setPrice(BigDecimal.TEN);
        invalid.setRestaurant(restaurant1);

        Meal result = mealService.createMeal(invalid);
        assertThat(result).isNull();
        verify(mealRepository, never()).save(any());

        // Test negative price
        invalid.setMeal("meal");
        invalid.setPrice(BigDecimal.valueOf(-5));
        result = mealService.createMeal(invalid);
        assertThat(result).isNull();
        verify(mealRepository, never()).save(any());

        // Test missing restaurant
        invalid.setPrice(BigDecimal.TEN);
        invalid.setRestaurant(null);
        result = mealService.createMeal(invalid);
        assertThat(result).isNull();
        verify(mealRepository, never()).save(any());
    }

    @Test
    void whenGetExistingMealById_thenReturnMeal() {
        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal1));
        Meal found = mealService.getMealById(1L);
        assertThat(found).isEqualTo(meal1);
    }

    @Test
    void whenGetNonExistingMealById_thenReturnNull() {
        when(mealRepository.findById(999L)).thenReturn(Optional.empty());
        Meal found = mealService.getMealById(999L);
        assertThat(found).isNull();
    }

    @Test
    void whenGetAllMealsForExistingRestaurant_thenReturnList() {
        when(mealRepository.findAllByRestaurantId(1L)).thenReturn(Optional.of(List.of(meal1)));
        List<Meal> meals = mealService.getAllMealsByRestaurantId(1L);
        assertThat(meals).hasSize(1).containsExactly(meal1);
    }

    @Test
    void whenGetMealsForNonExistingRestaurant_thenReturnEmptyList() {
        when(mealRepository.findAllByRestaurantId(999L)).thenReturn(Optional.of(List.of()));
        List<Meal> meals = mealService.getAllMealsByRestaurantId(999L);
        assertThat(meals).isEmpty();
    }

    @Test
    void whenUpdateExistingMeal_thenReturnUpdatedMeal() {
        Meal updatedMeal = new Meal();
        updatedMeal.setId(1L);
        updatedMeal.setMeal("updated meal");
        updatedMeal.setPrice(BigDecimal.valueOf(15));
        updatedMeal.setDate(LocalDate.now().plusDays(1));
        updatedMeal.setRestaurant(restaurant1);

        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal1));
        when(mealRepository.save(any(Meal.class))).thenReturn(updatedMeal);

        Meal result = mealService.updateMeal(updatedMeal);

        assertThat(result.getMeal()).isEqualTo("updated meal");
        assertThat(result.getPrice()).isEqualTo(BigDecimal.valueOf(15));
        verify(mealRepository).save(meal1);
    }

    @Test
    void whenUpdateNonExistingMeal_thenReturnNull() {
        Meal nonExisting = new Meal();
        nonExisting.setId(999L);

        when(mealRepository.findById(999L)).thenReturn(Optional.empty());

        Meal result = mealService.updateMeal(nonExisting);
        assertThat(result).isNull();
        verify(mealRepository, never()).save(any());
    }

    @Test
    void whenDeleteExistingMeal_thenReturnTrue() {
        when(mealRepository.existsById(1L)).thenReturn(true);
        boolean result = mealService.deleteMealById(1L);
        assertThat(result).isTrue();
        verify(mealRepository).deleteById(1L);
    }

    @Test
    void whenDeleteNonExistingMeal_thenReturnFalse() {
        when(mealRepository.existsById(999L)).thenReturn(false);
        boolean result = mealService.deleteMealById(999L);
        assertThat(result).isFalse();
        verify(mealRepository, never()).deleteById(any());
    }

}