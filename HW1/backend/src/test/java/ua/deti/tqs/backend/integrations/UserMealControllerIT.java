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
import ua.deti.tqs.backend.entities.UserMeal;
import ua.deti.tqs.backend.entities.utils.UserRole;
import ua.deti.tqs.backend.entities.utils.WeatherIPMA;
import ua.deti.tqs.backend.repositories.MealRepository;
import ua.deti.tqs.backend.repositories.RestaurantRepository;
import ua.deti.tqs.backend.repositories.UserMealRepository;
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
import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.not;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class UserMealControllerIT {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @LocalServerPort
    private int port;

    @Autowired
    private UserMealRepository userMealRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MealRepository mealRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @MockitoBean
    private WeatherService weatherService;

    private User staffUser;
    private User regularUser;
    private Meal availableMeal;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        cleanDatabase();

        // Create test users
        staffUser = userRepository.save(new User(null, "staff", "staffpass", UserRole.STAFF));
        regularUser = userRepository.save(new User(null, "user", "userpass", UserRole.USER));

        // Create test restaurant and meal
        Restaurant restaurant = restaurantRepository.save(new Restaurant(null, "Test Restaurant", "Lisbon", 2));
        availableMeal = mealRepository.save(new Meal(null, restaurant, BigDecimal.TEN,  LocalDateTime.now().plusHours(1),"Test Meal"  ));

        // Stub weather service
        when(weatherService.getLocationId("Lisbon")).thenReturn(Optional.of(1110600));
        when(weatherService.getForecastByLocation(1110600)).thenReturn(List.of(
                new Forecast("15", "25", "5", 2, 1, 1110600, "0", LocalDateTime.now().toString(), "N", 1, LocalDateTime.now().toString(), 24)
        ));
        when(weatherService.getWeatherForDate(anyList(), any(LocalDateTime.class)))
                .thenReturn(Optional.of(new WeatherIPMA("5", "15", "25", "N", "0", 2, 1)));
    }

    private void cleanDatabase() {
        userMealRepository.deleteAll();
        mealRepository.deleteAll();
        restaurantRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void whenCreateValidBooking_thenReturnCreated() {
        UserMeal booking = new UserMeal();
        booking.setUser(regularUser);
        booking.setMeal(availableMeal);
        booking.setIsCheck(true);
        booking.setCode("CODE123");

        given()
                .auth().preemptive().basic("user", "userpass")
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .post(Constants.API_PATH_PRIVATE_V1 + "user-meal")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("meal.meal", equalTo("Test Meal"))
                .body("code", not(equalTo(booking.getCode())))
                .body("isCheck", equalTo(false));

        assertThat(userMealRepository.count()).isEqualTo(1);
    }

    @Test
    void whenCreateOverlappingBooking_thenReturnBadRequest() {
        // Create first booking
        userMealRepository.save(new UserMeal(null, regularUser, availableMeal, false, "CODE123"));

        // Try to book same time slot
        UserMeal overlappingBooking = new UserMeal();
        overlappingBooking.setUser(regularUser);
        overlappingBooking.setMeal(availableMeal);

        given()
                .auth().preemptive().basic("user", "userpass")
                .contentType(ContentType.JSON)
                .body(overlappingBooking)
                .when()
                .post(Constants.API_PATH_PRIVATE_V1 + "user-meal")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenCreateBookingForFullRestaurant_thenReturnBadRequest() {
        // Fill all seats
        userMealRepository.save(new UserMeal(null, regularUser, availableMeal, false, "CODE1"));
        userMealRepository.save(new UserMeal(null, staffUser, availableMeal, false, "CODE2"));

        UserMeal newBooking = new UserMeal();
        newBooking.setUser(regularUser);
        newBooking.setMeal(availableMeal);

        given()
                .auth().preemptive().basic("user", "userpass")
                .contentType(ContentType.JSON)
                .body(newBooking)
                .when()
                .post(Constants.API_PATH_PRIVATE_V1 + "user-meal")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenGetOwnBooking_thenReturnWithWeather() {
        UserMeal booking = userMealRepository.save(new UserMeal(null, regularUser, availableMeal, false, "CUM-68d30a95"));

        given()
                .auth().preemptive().basic("user", "userpass")
                .pathParam("id", booking.getId())
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "user-meal/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("weatherIPMA.tempMin", equalTo("15"));
    }

    @Test
    void whenGetOthersBooking_thenReturnNotFound() {
        UserMeal booking = userMealRepository.save(new UserMeal(null, staffUser, availableMeal, false, "CODE123"));

        given()
                .auth().preemptive().basic("user", "userpass")
                .pathParam("id", booking.getId())
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "user-meal/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenGetAllUserBookings_thenReturnList() {
        userMealRepository.save(new UserMeal(null, regularUser, availableMeal, false, "CUM-d7a84628"));

        given()
                .auth().preemptive().basic("user", "userpass")
                .pathParam("userId", regularUser.getId())
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "user-meal/all/{userId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));

    }

    @Test
    void whenStaffGetRestaurantBookings_thenReturnList() {
        userMealRepository.save(new UserMeal(null, regularUser, availableMeal, false, "CODE123"));

        given()
                .auth().preemptive().basic("staff", "staffpass")
                .pathParam("restaurantId", availableMeal.getRestaurant().getId())
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "user-meal/all/restaurant/{restaurantId}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(1));
    }

    @Test
    void whenNonStaffGetRestaurantBookings_thenReturnForbidden() {
        given()
                .auth().preemptive().basic("user", "userpass")
                .pathParam("restaurantId", availableMeal.getRestaurant().getId())
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "user-meal/all/restaurant/{restaurantId}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenStaffUpdateCheckStatus_thenReturnUpdated() {
        UserMeal booking = userMealRepository.save(new UserMeal(null, regularUser, availableMeal, false, "CODE123"));
        booking.setIsCheck(true);

        given()
                .auth().preemptive().basic("staff", "staffpass")
                .contentType(ContentType.JSON)
                .body(booking)
                .when()
                .put(Constants.API_PATH_PRIVATE_V1 + "user-meal")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("isCheck", equalTo(true));
    }

    @Test
    void whenUserDeleteOwnBooking_thenReturnNoContent() {
        UserMeal booking = userMealRepository.save(new UserMeal(null, regularUser, availableMeal, false, "CODE123"));

        given()
                .auth().preemptive().basic("user", "userpass")
                .pathParam("id", booking.getId())
                .when()
                .delete(Constants.API_PATH_PRIVATE_V1 + "user-meal/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(userMealRepository.existsById(booking.getId())).isFalse();
    }

    @Test
    void whenDeleteOthersBooking_thenReturnNotFound() {
        UserMeal booking = userMealRepository.save(new UserMeal(null, staffUser, availableMeal, false, "CODE123"));

        given()
                .auth().preemptive().basic("user", "userpass")
                .pathParam("id", booking.getId())
                .when()
                .delete(Constants.API_PATH_PRIVATE_V1 + "user-meal/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}