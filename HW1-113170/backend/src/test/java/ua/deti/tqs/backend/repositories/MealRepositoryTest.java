package ua.deti.tqs.backend.repositories;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
class MealRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private MealRepository mealRepository;

    private Meal meal1;
    private Meal meal2;

    private Restaurant restaurant1;

    @BeforeEach
    void setUp() {
        restaurant1 = new Restaurant();
        restaurant1.setName("restaurant1");
        restaurant1.setLocation("location1");
        restaurant1.setSeats(10);

        entityManager.persist(restaurant1);


        meal1 = new Meal();
        meal1.setName("meal1");
        meal1.setRestaurant(restaurant1);
        meal1.setDate(LocalDateTime.now().truncatedTo(ChronoUnit.MICROS));
        meal1.setPrice(BigDecimal.valueOf(10));

        meal2 = new Meal();
        meal2.setName("meal2");
        meal2.setRestaurant(restaurant1);
        meal2.setDate(LocalDateTime.now());
        meal2.setPrice(BigDecimal.valueOf(20));

        entityManager.persist(meal1);
        entityManager.persist(meal2);
    }

    @Test
    void whenFindById_thenReturnMeal() {
        Meal found = mealRepository.findById(meal1.getId()).orElse(null);
        assertThat(found).isEqualTo(meal1);

    }

    @Test
    void whenFindByInvalidId_thenReturnNull() {
        Meal fromDb = mealRepository.findById(-111L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindAllByRestaurantId_thenReturnMeals() {
        Iterable<Meal> allMeals = mealRepository.findAllByRestaurantId(restaurant1.getId()).orElse(null);
        assertThat(allMeals).hasSize(2).contains(meal1, meal2);
    }

    @Test
    void whenFindAllByInvalidRestaurantId_thenReturnEmpty() {
        Iterable<Meal> allMeals = mealRepository.findAllByRestaurantId(-111L).orElse(null);
        assertThat(allMeals).isEmpty();
    }

    @Test
    void whenFindMealByMealAndRestaurantAndDate_thenReturnMeal() {
        Meal found = mealRepository.findMealByNameAndRestaurantAndDate(meal1.getName(), restaurant1, meal1.getDate()).orElse(null);
        assertThat(found).isEqualTo(meal1);
    }

    @Test
    void whenFindMealByInvalidMealAndRestaurantAndDate_thenReturnNull() {
        Meal fromDb = mealRepository.findMealByNameAndRestaurantAndDate("invalid", restaurant1, LocalDateTime.now()).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenDeleteMealById_thenMealShouldNotExist() {
        mealRepository.deleteById(meal1.getId());

        assertThat(mealRepository.findById(meal1.getId())).isEmpty();
    }

    @Test
    void whenUpdateMeal_thenMealShouldBeUpdated() {
        meal1.setName("updatedMeal");
        meal1.setPrice(BigDecimal.valueOf(15));

        Meal updated = mealRepository.save(meal1);

        assertThat(updated.getName()).isEqualTo("updatedMeal");
        assertThat(updated.getPrice()).isEqualTo(BigDecimal.valueOf(15));
    }

    @Test
    void whenCreateMeal_thenReturnMeal() {
        Meal newMeal = new Meal();
        newMeal.setName("newMeal");
        newMeal.setRestaurant(restaurant1);
        newMeal.setDate(LocalDateTime.now());
        newMeal.setPrice(BigDecimal.valueOf(25));

        Meal created = mealRepository.save(newMeal);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getName()).isEqualTo("newMeal");
        assertThat(created.getPrice()).isEqualTo(BigDecimal.valueOf(25));
    }

    @Test
    void whenFindByIdWithRestaurantLock_thenReturnMealWithRestaurant() {
        Optional<Meal> found = mealRepository.findByIdWithRestaurantLock(meal1.getId());

        assertThat(found).isPresent().contains(meal1);
        assertThat(found.get().getRestaurant()).isEqualTo(restaurant1);
    }


}
