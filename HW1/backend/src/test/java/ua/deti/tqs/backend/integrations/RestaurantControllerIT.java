package ua.deti.tqs.backend.integrations;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.deti.tqs.backend.entities.Restaurant;
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.repositories.RestaurantRepository;
import ua.deti.tqs.backend.repositories.UserRepository;
import ua.deti.tqs.backend.utils.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
public class RestaurantControllerIT {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @LocalServerPort
    private int port;

    @Autowired
    private RestaurantRepository restaurantRepository;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        cleanDatabase();
    }

    private void cleanDatabase() {
        userRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void whenPostValidRestaurant_thenCreateRestaurant() {
        Restaurant restaurant = new Restaurant(null, "Test Restaurant", "Test Location", 50);

        given()
                .contentType(ContentType.JSON)
                .body(restaurant)
                .when()
                .post(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo("Test Restaurant"))
                .body("location", equalTo("Test Location"))
                .body("seats", equalTo(50));

        List<Restaurant> found = restaurantRepository.findAll();
        assertThat(found).hasSize(1);
        assertThat(found.get(0).getName()).isEqualTo("Test Restaurant");
    }

    @Test
    void whenPostInvalidRestaurant_thenReturnBadRequest() {
        Restaurant restaurant = new Restaurant(null, "", "Test Location", 50);

        given()
                .contentType(ContentType.JSON)
                .body(restaurant)
                .when()
                .post(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        assertThat(restaurantRepository.count()).isZero();
    }

    @Test
    void whenPostDuplicateName_thenReturnBadRequest() {
        Restaurant restaurant1 = new Restaurant(null, "Duplicate", "Loc1", 50);
        restaurantRepository.save(restaurant1);

        Restaurant restaurant2 = new Restaurant(null, "Duplicate", "Loc2", 60);

        given()
                .contentType(ContentType.JSON)
                .body(restaurant2)
                .when()
                .post(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        assertThat(restaurantRepository.count()).isEqualTo(1);
    }

    @Test
    void whenGetExistingRestaurantById_thenReturnRestaurant() {
        Restaurant saved = restaurantRepository.save(new Restaurant(null, "Test", "Loc", 50));

        given()
                .pathParam("id", saved.getId())
                .when()
                .get(Constants.API_PATH_V1 + "restaurant/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", equalTo(saved.getId().intValue()))
                .body("name", equalTo("Test"));
    }

    @Test
    void whenGetNonExistingRestaurantById_thenReturnNotFound() {
        given()
                .pathParam("id", 9999)
                .when()
                .get(Constants.API_PATH_V1 + "restaurant/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenGetExistingRestaurantByName_thenReturnRestaurant() {
        restaurantRepository.save(new Restaurant(null, "Test Name", "Loc", 50));

        given()
                .pathParam("name", "Test Name")
                .when()
                .get(Constants.API_PATH_V1 + "restaurant/name/{name}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("Test Name"));
    }

    @Test
    void whenGetNonExistingRestaurantByName_thenReturnNotFound() {
        given()
                .pathParam("name", "NonExisting")
                .when()
                .get(Constants.API_PATH_V1 + "restaurant/name/{name}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenGetAllRestaurants_thenReturnAll() {
        restaurantRepository.saveAll(List.of(
                new Restaurant(null, "R1", "L1", 50),
                new Restaurant(null, "R2", "L2", 60)
        ));

        List<Restaurant> restaurants = given()
                .when()
                .get(Constants.API_PATH_V1 + "restaurant/all")
                .then()
                .statusCode(HttpStatus.OK.value())
                .extract().jsonPath().getList(".", Restaurant.class);

        assertThat(restaurants).hasSize(2);
    }

    @Test
    void whenUpdateExistingRestaurant_thenUpdateAndReturnOk() {
        Restaurant original = restaurantRepository.save(new Restaurant(null, "Old", "Old Loc", 50));

        Restaurant updateData = new Restaurant(original.getId(), "New", "New Loc", 60);

        given()
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("New"))
                .body("location", equalTo("New Loc"))
                .body("seats", equalTo(60));

        Restaurant updated = restaurantRepository.findById(original.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("New");
        assertThat(updated.getSeats()).isEqualTo(60);
    }

    @Test
    void whenUpdateNonExistingRestaurant_thenReturnBadRequest() {
        Restaurant updateData = new Restaurant(9999L, "New", "New Loc", 60);

        given()
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenDeleteExistingRestaurant_thenReturnNoContent() {
        Restaurant saved = restaurantRepository.save(new Restaurant(null, "Test", "Loc", 50));

        given()
                .pathParam("id", saved.getId())
                .when()
                .delete(Constants.API_PATH_V1 + "restaurant/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(restaurantRepository.existsById(saved.getId())).isFalse();
    }

    @Test
    void whenDeleteNonExistingRestaurant_thenReturnNotFound() {
        given()
                .pathParam("id", 9999)
                .when()
                .delete(Constants.API_PATH_V1 + "restaurant/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenCreateRestaurantWithTrimmedFields_thenTrimmed() {
        Restaurant restaurant = new Restaurant(null, "  Test  ", "  Location  ", 50);

        given()
                .contentType(ContentType.JSON)
                .body(restaurant)
                .when()
                .post(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("name", equalTo("Test"))
                .body("location", equalTo("Location"));

        Restaurant saved = restaurantRepository.findAll().get(0);
        assertThat(saved.getName()).isEqualTo("Test");
        assertThat(saved.getLocation()).isEqualTo("Location");
    }

    @Test
    void whenUpdateRestaurantPartially_thenUpdateOnlyChangedFields() {
        Restaurant original = restaurantRepository.save(new Restaurant(null, "OldName", "OldLoc", 50));

        Restaurant updateData = new Restaurant(original.getId(), "NewName", null, null);

        given()
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("name", equalTo("NewName"))
                .body("location", equalTo("OldLoc"))
                .body("seats", equalTo(50));

        Restaurant updated = restaurantRepository.findById(original.getId()).orElseThrow();
        assertThat(updated.getName()).isEqualTo("NewName");
    }

    @Test
    void whenUpdateWithInvalidSeats_thenNoChange() {
        Restaurant original = restaurantRepository.save(new Restaurant(null, "Name", "Loc", 50));

        Restaurant updateData = new Restaurant(original.getId(), null, null, -5);

        given()
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());


        Restaurant updated = restaurantRepository.findById(original.getId()).orElseThrow();
        assertThat(updated.getSeats()).isEqualTo(50);
    }

    @Test
    void whenUpdateAllFieldsInvalid_thenReturnBadRequest() {
        Restaurant original = restaurantRepository.save(new Restaurant(null, "Name", "Loc", 50));

        Restaurant updateData = new Restaurant(original.getId(), "", "", 0);

        given()
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_V1 + "restaurant")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());

        Restaurant notUpdated = restaurantRepository.findById(original.getId()).orElseThrow();
        assertThat(notUpdated.getName()).isEqualTo("Name");
        assertThat(notUpdated.getSeats()).isEqualTo(50);
    }
}
