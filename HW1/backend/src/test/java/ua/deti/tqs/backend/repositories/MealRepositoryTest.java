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
import java.time.LocalDate;
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
    private Meal meal3;

    private Restaurant restaurant1;
    private Restaurant restaurant2;

    @BeforeEach
    void setUp() {
        restaurant1 = new Restaurant();
        restaurant1.setName("restaurant1");
        restaurant1.setLocation("location1");
        restaurant1.setSeats(10);

        restaurant2 = new Restaurant();
        restaurant2.setName("restaurant2");
        restaurant2.setLocation("location2");
        restaurant2.setSeats(20);

        entityManager.persist(restaurant1);
        entityManager.persist(restaurant2);


        meal1 = new Meal();
        meal1.setMeal("meal1");
        meal1.setRestaurant(restaurant1);
        meal1.setDate(LocalDate.now());
        meal1.setPrice(BigDecimal.valueOf(10));

        meal2 = new Meal();
        meal2.setMeal("meal2");
        meal2.setRestaurant(restaurant1);
        meal2.setDate(LocalDate.now());
        meal2.setPrice(BigDecimal.valueOf(20));

        meal3 = new Meal();
        meal3.setMeal("meal3");
        meal3.setRestaurant(restaurant2);
        meal3.setDate(LocalDate.now());
        meal3.setPrice(BigDecimal.valueOf(30));

        entityManager.persist(meal1);
        entityManager.persist(meal2);
        entityManager.persist(meal3);
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
        Meal found = mealRepository.findMealByMealAndRestaurantAndDate(meal1.getMeal(), restaurant1, meal1.getDate()).orElse(null);
        assertThat(found).isEqualTo(meal1);
    }

    @Test
    void whenFindMealByInvalidMealAndRestaurantAndDate_thenReturnNull() {
        Meal fromDb = mealRepository.findMealByMealAndRestaurantAndDate("invalid", restaurant1, LocalDate.now()).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenDeleteMealById_thenMealShouldNotExist() {
        mealRepository.deleteById(meal1.getId());

        assertThat(mealRepository.findById(meal1.getId())).isEmpty();
    }

    @Test
    void whenUpdateMeal_thenMealShouldBeUpdated() {
        meal1.setMeal("updatedMeal");
        meal1.setPrice(BigDecimal.valueOf(15));

        Meal updated = mealRepository.save(meal1);

        assertThat(updated.getMeal()).isEqualTo("updatedMeal");
        assertThat(updated.getPrice()).isEqualTo(BigDecimal.valueOf(15));
    }

    @Test
    void whenCreateMeal_thenReturnMeal() {
        Meal newMeal = new Meal();
        newMeal.setMeal("newMeal");
        newMeal.setRestaurant(restaurant1);
        newMeal.setDate(LocalDate.now());
        newMeal.setPrice(BigDecimal.valueOf(25));

        Meal created = mealRepository.save(newMeal);

        assertThat(created.getId()).isNotNull();
        assertThat(created.getMeal()).isEqualTo("newMeal");
        assertThat(created.getPrice()).isEqualTo(BigDecimal.valueOf(25));
    }

    @Test
    void whenFindByIdWithRestaurantLock_thenReturnMealWithRestaurant() {
        Optional<Meal> found = mealRepository.findByIdWithRestaurantLock(meal1.getId());

        assertThat(found).isPresent();
        assertThat(found.get()).isEqualTo(meal1);
        assertThat(found.get().getRestaurant()).isEqualTo(restaurant1);
    }


}
