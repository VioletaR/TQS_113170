//package ua.deti.tqs.backend.services;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ua.deti.tqs.backend.entities.Meal;
//import ua.deti.tqs.backend.entities.Restaurant;
//import ua.deti.tqs.backend.repositories.MealRepository;
//import ua.deti.tqs.backend.services.interfaces.MealService;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Optional;
//
//import static org.mockito.Mockito.when;
//
//@ExtendWith(MockitoExtension.class)
//public class MealServiceTest {
//    @Mock(lenient = true)
//    private MealRepository mealRepository;
//
//    @InjectMocks
//    private MealServiceImpl mealService;
//
//    @BeforeEach
//    public void setUp() {
//        Restaurant restaurant = new Restaurant(1L, "Restaurant", 10);
//        Restaurant restaurant1 = new Restaurant(2L, "Restaurant1", 10);
//
//        Meal meal = new Meal(1L, restaurant, new BigDecimal(10), LocalDate.now(), "Meal");
//        Meal meal1 = new Meal(2L, restaurant, new BigDecimal(10), LocalDate.now(), "Meal1");
//
//        List<Meal> meals = new ArrayList<>();
//        meals.add(meal);
//        meals.add(meal1);
//
//        when(mealRepository.findById(1L)).thenReturn(Optional.of(meal));
//        when(mealRepository.findById(123L)).thenReturn(Optional.empty());
//
//        when(mealRepository.findAllByRestaurantId(1L)).thenReturn(meals);
//        when(mealRepository.findAllByRestaurantId(2L)).thenReturn(new ArrayList<>());
//        when(mealRepository.findAllByRestaurantId(123L)).thenReturn(new ArrayList<>());
//
//
//
////        when(mealRepository.delete(meal)).thenReturn(null); // how can i test this?
//
//    }
//
//    @Test
//    public void testGetMealById() {
//        Meal meal = mealService.getMealById(1L);
//        assert(meal != null);
//    }
//
//    @Test
//    public void testGetMealByIdNotFound() {
//        Meal meal = mealService.getMealById(123L);
//        assert(meal == null);
//    }
//
//    @Test
//    public void testGetMealByInvalidId() {
//        Meal meal = mealService.getMealById(0L);
//        assert(meal == null);
//    }
//
//    @Test
//    public void testGetAllMealsByRestaurantId() {
//        List<Meal> meals = mealService.getAllMealsByRestaurantId(1L);
//        assert(meals.size() == 2);
//    }
//
//    @Test
//    public void testGetAllMealsByRestaurantIdWithoutAnyMeals() {
//        List<Meal> meals = mealService.getAllMealsByRestaurantId(2L);
//        assert(meals.isEmpty());
//    }
//
//    @Test
//    public void testGetAllMealsByRestaurantIdThatDoenstExist() {
//        List<Meal> meals = mealService.getAllMealsByRestaurantId(123L);
//        assert(meals.isEmpty());
//    }
//
//    @Test
//    public void testGetAllMealsByInvalidRestaurantId() {
//        List<Meal> meals = mealService.getAllMealsByRestaurantId(0L);
//        assert(meals.isEmpty());
//    }
//
//    @Test
//    public void testDeleteMealById() {
//        boolean result = mealService.deleteMealById(1L);
//        assert(result);
//    }
//
//    @Test
//    public void testDeleteMealByIdNotFound() {
//        boolean result = mealService.deleteMealById(123L);
//        assert(result);
//    }
//
//    @Test
//    public void testDeleteMealByInvalidId() {
//        boolean result = mealService.deleteMealById(0L);
//        assert(result);
//    }
//
//    @Test
//    public void testCreateMeal() {
//        Restaurant restaurant = new Restaurant(1L, "Restaurant", 10);
//
//        Meal meal = new Meal(1L, restaurant, new BigDecimal(10), LocalDate.now(), "Meal");
//
//        when(mealRepository.save(meal)).thenReturn(meal);
//        Meal newMeal = mealService.createMeal(meal);
//        assert(newMeal != null);
//    }
//
//    @Test
//    public void testCreateMealWithNullRestaurant() {
//        Meal meal = new Meal(1L, null, new BigDecimal(10), LocalDate.now(), "Meal");
//        Meal newMeal = mealService.createMeal(meal);
//        assert(newMeal == null);
//        // TODO SEE THIS WITH ANDRE
//    }
//
//    @Test
//    public void testUpdateMeal() {
//        Restaurant restaurant = new Restaurant(1L, "Restaurant", 10);
//        Meal meal = new Meal(1L, restaurant, new BigDecimal(20), LocalDate.now(), "updatedMeal");
//
//        Meal updatedMeal = mealService.updateMeal(meal);
//        assert(updatedMeal.equals(meal));
//    }
//
//
//}
