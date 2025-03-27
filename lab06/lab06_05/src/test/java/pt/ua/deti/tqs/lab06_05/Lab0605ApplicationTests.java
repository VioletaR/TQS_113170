package pt.ua.deti.tqs.lab06_05;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import pt.ua.deti.tqs.lab06_05.model.Car;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItem;

@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Lab0605ApplicationTests {
    @LocalServerPort
    private int port;


    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    void whenPostCar_thenCarIsCreated() {
        Car car = new Car();
        car.setMaker("Tesla");
        car.setModel("Model X");

        given()
                .contentType("application/json")
                .body(car)
                .when()
                .post("/api/cars")
                .then()
                .statusCode(201)
                .body("maker", equalTo("Tesla"))
                .body("model", equalTo("Model X"));

        // Verify GET returns the created car
        given().when()
                .get("/api/cars")
                .then()
                .statusCode(200)
                .body("maker", hasItem("Tesla"))
                .body("model", hasItem("Model X"));
    }

    @Test
    void whenGetCarById_thenReturnCar() {
        // First, create a car
        Car car = new Car();
        car.setMaker("Ford");
        car.setModel("Focus");

        Integer carId = given() // aposto que o rest assured nao lida bem com longs
                .contentType("application/json")
                .body(car)
                .when()
                .post("/api/cars")
                .then()
                .extract()
                .path("carId");

        // Then retrieve it by ID
        given()
                .pathParam("id", carId)
                .when()
                .get("/api/cars/{id}")
                .then()
                .statusCode(200)
                .body("maker", equalTo("Ford"))
                .body("model", equalTo("Focus"));
    }
}