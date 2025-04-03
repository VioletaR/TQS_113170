package ua.deti.tqs.backend.repositories;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import ua.deti.tqs.backend.entities.Restaurant;

import static org.assertj.core.api.Assertions.assertThat;



@DataJpaTest
@ActiveProfiles("test")
public class RestaurantRepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private RestaurantRepository restaurantRepository;

    private Restaurant restaurant1;
    private Restaurant restaurant2;


    @BeforeEach
    void setUp() {
        restaurant1 = new Restaurant();

        restaurant1.setName("restaurant1");
        restaurant1.setDistrict("district1");
        restaurant1.setSeats(10);

        restaurant2 = new Restaurant();

        restaurant2.setName("restaurant2");
        restaurant2.setDistrict("district2");
        restaurant2.setSeats(20);

        entityManager.persist(restaurant1);
        entityManager.persist(restaurant2);

    }

    @Test
    void whenCreateRestaurant_thenReturnRestaurant() {
        Restaurant found = restaurantRepository.findById(restaurant1.getId()).orElse(null);
        assertThat(found).isEqualTo(restaurant1);
    }

    @Test
    void whenInvalidRestaurantId_thenReturnNull() {
        Restaurant fromDb = restaurantRepository.findById(-111L).orElse(null);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindAllRestaurants_thenReturnAllRestaurants() {
        Iterable<Restaurant> allRestaurants = restaurantRepository.findAll();
        assertThat(allRestaurants).hasSize(2).contains(restaurant1, restaurant2);
    }

    @Test
    void whenDeleteRestaurantById_thenRestaurantShouldNotExist() {
        restaurantRepository.deleteById(restaurant1.getId());

        assertThat(restaurantRepository.findById(restaurant1.getId())).isEmpty();
    }

    @Test
    void whenUpdateRestaurant_thenRestaurantShouldBeUpdated() {

        restaurant1.setName("updatedRestaurant");
        restaurant1.setDistrict("updatedDistrict");
        restaurant1.setSeats(2);
        restaurantRepository.save(restaurant1);

        Restaurant updatedRestaurant = restaurantRepository.findById(restaurant1.getId()).orElse(null);
        assertThat(updatedRestaurant).isNotNull();
        assertThat(updatedRestaurant.getName()).isEqualTo("updatedRestaurant");
        assertThat(updatedRestaurant.getDistrict()).isEqualTo("updatedDistrict");
        assertThat(updatedRestaurant.getSeats()).isEqualTo(2);
    }

    @Test
    void whenFindRestaurantByName_thenReturnRestaurant() {
        Restaurant found = restaurantRepository.findByName(restaurant1.getName()).orElse(null);
        assertThat(found).isEqualTo(restaurant1);
    }

    @Test
    void whenFindRestaurantByInvalidName_thenReturnNull() {
        Restaurant found = restaurantRepository.findByName("invalidName").orElse(null);
        assertThat(found).isNull();
    }
}
