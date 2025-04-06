package ua.deti.tqs.backend.integrations;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.deti.tqs.backend.dtos.Forecast;
import ua.deti.tqs.backend.dtos.MealDTO;
import ua.deti.tqs.backend.entities.Meal;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.repositories.MealRepository;
import ua.deti.tqs.backend.repositories.RestaurantRepository;
import ua.deti.tqs.backend.repositories.UserRepository;
import ua.deti.tqs.backend.services.interfaces.WeatherService;
import ua.deti.tqs.backend.utils.Constants;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class MealControllerIT {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @LocalServerPort
    private int port;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @MockitoBean
    private WeatherService weatherService;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        cleanDatabase();
        resetMocks();
    }

    private void cleanDatabase() {
        mealRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    private void resetMocks() {
        Mockito.reset(weatherService);
    }

    private Restaurant createRestaurant(String name, String location) {
        return restaurantRepository.save(new Restaurant(null, name, location, 50));
    }

    private Meal createMeal(Restaurant restaurant, String mealName, LocalDateTime date) {
        Meal meal = new Meal(null, restaurant, BigDecimal.TEN, date,mealName);
        return mealRepository.save(meal);
    }

    @Test
    void whenPostValidMeal_thenCreateMeal() {
        Restaurant restaurant = createRestaurant("Test Restaurant", "Lisbon");
        Meal meal = new Meal(null,restaurant,BigDecimal.TEN,  LocalDateTime.now(),"Test Meal");

        given()
                .contentType(ContentType.JSON)
                .body(meal)
                .when()
                .post(Constants.API_PATH_V1 + "meals")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("meal", equalTo("Test Meal"))
                .body("price", equalTo(10));

        assertThat(mealRepository.count()).isEqualTo(1);
    }

    @Test
    void whenPostMealWithInvalidData_thenReturnBadRequest() {
        Meal meal = new Meal(null, null, BigDecimal.ZERO, LocalDateTime.now(), "");

        given()
                .contentType(ContentType.JSON)
                .body(meal)
                .when()
                .post(Constants.API_PATH_V1 + "meals")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        assertThat(mealRepository.count()).isZero();
    }

    @Test
    void whenGetExistingMeal_thenReturnMealWithWeather() {
        Restaurant restaurant = createRestaurant("Weather Restaurant", "Lisbon");
        LocalDateTime mealDate = LocalDateTime.of(2023, 10, 10, 12, 0);
        Meal meal = createMeal(restaurant, "Weather Meal", mealDate);

        // Stub weather service responses
        when(weatherService.getLocationId("Lisbon")).thenReturn(Optional.of(1110600));
        when(weatherService.getForecastByLocation(1110600)).thenReturn(List.of(
                new Forecast("15", "25", "5", 2, 1, 1110600, "0", "2023-10-10T12:00:00", "N", 1, "2023-10-10T00:00:00", 24)
        ));
        when(weatherService.getWeatherForDate(anyList(), eq(mealDate)))
                .thenReturn(Optional.of(new WeatherIPMA("5", "15", "25", "N", "0", 2, 1)));

        given()
                .pathParam("id", meal.getId())
                .when()
                .get(Constants.API_PATH_V1 + "meals/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("meal.meal", equalTo("Weather Meal"))
                .body("weatherIPMA.tempMin", equalTo("15"))
                .body("weatherIPMA.tempMax", equalTo("25"));
    }

    @Test
    void whenGetNonExistingMeal_thenReturnNotFound() {
        given()
                .pathParam("id", 9999)
                .when()
                .get(Constants.API_PATH_V1 + "meals/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenGetMealWithNoWeatherData_thenReturnMealWithoutWeather() {
        Restaurant restaurant = createRestaurant("No Weather Restaurant", "Unknown");
        Meal meal = createMeal(restaurant, "No Weather Meal", LocalDateTime.now());

        when(weatherService.getLocationId("Unknown")).thenReturn(Optional.empty());

        given()
                .pathParam("id", meal.getId())
                .when()
                .get(Constants.API_PATH_V1 + "meals/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("meal.meal", equalTo("No Weather Meal"))
                .body("weatherIPMA", nullValue());
    }

    @Test
    void whenGetAllMealsForRestaurant_thenReturnListWithWeather() {
        Restaurant restaurant = createRestaurant("Multi-Meal Restaurant", "Porto");
        Meal meal1 = createMeal(restaurant, "Meal 1", LocalDateTime.now());
        Meal meal2 = createMeal(restaurant, "Meal 2", LocalDateTime.now().plusDays(1));

        // Stub weather service for Porto
        when(weatherService.getLocationId("Porto")).thenReturn(Optional.of(1130600));
        when(weatherService.getForecastByLocation(1130600)).thenReturn(List.of(
                new Forecast("10", "20", "3", 1, 0, 1130600, "0", "2023-10-10T12:00:00", "NE", 1, "2023-10-10T00:00:00", 24)
        ));
        when(weatherService.getWeatherForDate(anyList(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(new WeatherIPMA("3", "10", "20", "NE", "0", 1, 0)));

        List<MealDTO> meals = given()
                .pathParam("restaurantId", restaurant.getId())
                .when()
                .get(Constants.API_PATH_V1 + "meals/all/{restaurantId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", MealDTO.class);

        assertThat(meals).hasSize(2);
        assertThat(meals.get(0).getWeatherIPMA()).isNotNull();
        assertThat(meals.get(1).getWeatherIPMA()).isNotNull();
    }

    @Test
    void whenUpdateValidMeal_thenUpdateMealDetails() {
        Restaurant restaurant = createRestaurant("Update Test Restaurant", "Lisbon");
        Meal originalMeal = createMeal(restaurant, "Original Meal", LocalDateTime.now());
        Meal updateData = new Meal(originalMeal.getId(),restaurant,BigDecimal.TWO, LocalDateTime.now(),  "Updated Meal" );

        given()
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_V1 + "meals")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("meal", equalTo("Updated Meal"))
                .body("price", equalTo(2));

        Meal updated = mealRepository.findById(originalMeal.getId()).orElseThrow();
        assertThat(updated.getMeal()).isEqualTo("Updated Meal");
    }

    @Test
    void whenUpdateNonExistingMeal_thenReturnBadRequest() {
        Meal updateData = new Meal(9999L, null,BigDecimal.TEN, LocalDateTime.now(), "Ghost Meal");

        given()
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_V1 + "meals")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenDeleteExistingMeal_thenReturnNoContent() {
        Restaurant restaurant = createRestaurant("Delete Restaurant", "Lisbon");
        Meal meal = createMeal(restaurant, "To Delete", LocalDateTime.now());

        given()
                .pathParam("id", meal.getId())
                .when()
                .delete(Constants.API_PATH_V1 + "meals/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(mealRepository.existsById(meal.getId())).isFalse();
    }

    @Test
    void whenDeleteNonExistingMeal_thenReturnNotFound() {
        given()
                .pathParam("id", 9999)
                .when()
                .delete(Constants.API_PATH_V1 + "meals/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenCreateMealWithNonExistingRestaurant_thenReturnBadRequest() {
        Meal meal = new Meal(null,new Restaurant(9999L, "Ghost", "Nowhere", 100), BigDecimal.TEN, LocalDateTime.now(), "Orphan Meal");

        given()
                .contentType(ContentType.JSON)
                .body(meal)
                .when()
                .post(Constants.API_PATH_V1 + "meals")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        assertThat(mealRepository.count()).isZero();
    }

    @Test
    void whenGetMealWithForecastDateMismatch_thenReturnMealWithoutWeather() {
        Restaurant restaurant = createRestaurant("Date Mismatch Restaurant", "Faro");
        LocalDateTime mealDate = LocalDateTime.of(2024, 1, 1, 12, 0);
        Meal meal = createMeal(restaurant, "Date Test Meal", mealDate);

        // Stub forecasts with dates far from mealDate
        when(weatherService.getLocationId("Faro")).thenReturn(Optional.of(1140600));
        when(weatherService.getForecastByLocation(1140600)).thenReturn(List.of(
                new Forecast("10", "20", "3", 1, 0, 1140600, "0", "2023-12-25T00:00:00", "N", 1, "2023-12-25T00:00:00", 24)
        ));
        when(weatherService.getWeatherForDate(anyList(), eq(mealDate))).thenReturn(Optional.empty());

        given()
                .pathParam("id", meal.getId())
                .when()
                .get(Constants.API_PATH_V1 + "meals/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("weatherIPMA", nullValue());
    }
}