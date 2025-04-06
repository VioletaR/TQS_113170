package ua.deti.tqs.backend.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.UserMeal;
import ua.deti.tqs.backend.entities.utils.UserRole;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class UserMealRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private  UserMealRepository userMealRepository;

    private UserMeal userMeal1;

    private Restaurant restaurant1;
    private Meal meal1;
    private User user1;


    @BeforeEach
    void setUp() {
        restaurant1 = new Restaurant();
        restaurant1.setName("restaurant1");
        restaurant1.setLocation("location1");
        restaurant1.setSeats(10);
        entityManager.persist(restaurant1);

        user1 = new User();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setRole(UserRole.USER);
        entityManager.persist(user1);

        meal1 = new Meal();
        meal1.setMeal("meal1");
        meal1.setDate(LocalDateTime.now());
        meal1.setPrice(BigDecimal.valueOf(10));
        meal1.setRestaurant(restaurant1);
        entityManager.persist(meal1);

        userMeal1 = new UserMeal();
        userMeal1.setUser(user1);
        userMeal1.setMeal(meal1);
        userMeal1.setIsCheck(false);
        entityManager.persist(userMeal1);
    }

    @Test
    void whenFindByUserId_thenReturnUserMeals() {
        Optional<List<UserMeal>> found = userMealRepository.findAllByUserId(user1.getId());
        assertThat(found).isPresent();
        assertThat(found.get()).containsExactly(userMeal1);
    }

    @Test
    void whenFindByInvalidUserId_thenReturnEmpty() {
        Optional<List<UserMeal>> found = userMealRepository.findAllByUserId(999L);
        assertThat(found).isPresent();
        assertThat(found.get()).isEmpty();
    }

    @Test
    void whenFindByRestaurantId_thenReturnUserMeals() {
        Optional<List<UserMeal>> found = userMealRepository.findAllByMeal_RestaurantId(restaurant1.getId());
        assertThat(found).isPresent();
        assertThat(found.get()).containsExactly(userMeal1);
    }

    @Test
    void whenFindByInvalidRestaurantId_thenReturnEmpty() {
        Optional<List<UserMeal>> found = userMealRepository.findAllByMeal_RestaurantId(999L);
        assertThat(found).isPresent();
        assertThat(found.get()).isEmpty();
    }

    @Test
    void whenCountByMealId_thenReturnCount() {
        int count = userMealRepository.countByMealId(meal1.getId());
        assertThat(count).isEqualTo(1);
    }

    @Test
    void whenCountByInvalidMealId_thenReturnZero() {
        int count = userMealRepository.countByMealId(999L);
        assertThat(count).isZero();
    }
}
