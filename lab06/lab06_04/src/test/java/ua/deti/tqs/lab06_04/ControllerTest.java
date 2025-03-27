package ua.deti.tqs.lab06_04;


import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ua.deti.tqs.lab06_04.controller.CarController;
import ua.deti.tqs.lab06_04.entity.Car;
import ua.deti.tqs.lab06_04.service.CarManagerService;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(CarController.class)
public class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CarManagerService service;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    void whenPostCar_thenCreateCar() {
        Car savedCar = new Car(1L, "Tesla", "Model S");
        Car carToPost = new Car("Tesla", "Model S");
        when(service.save(any(Car.class))).thenReturn(savedCar);

        RestAssuredMockMvc.given()
                .contentType(ContentType.JSON)
                .body(carToPost)
                .when()
                .post("/api/v1/car")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("carId", equalTo(1),
                        "maker", equalTo("Tesla"),
                        "model", equalTo("Model S"));
    }

    @Test
    void givenTwoCars_whenGetCars_thenReturnJsonArray() {
        Car car1 = new Car(1L, "Toyota", "Corolla");
        Car car2 = new Car(2L, "Honda", "Civic");
        List<Car> allCars = Arrays.asList(car1, car2);
        when(service.getAllCars()).thenReturn(allCars);

        RestAssuredMockMvc.given()
                .when()
                .get("/api/v1/car")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("size()", equalTo(2))
                .body("[0].carId", equalTo(1),
                        "[0].maker", equalTo("Toyota"),
                        "[0].model", equalTo("Corolla"),
                        "[1].carId", equalTo(2),
                        "[1].maker", equalTo("Honda"),
                        "[1].model", equalTo("Civic"));
    }

    @Test
    void givenExistingCarId_whenGetCar_thenReturnCar() {
        Long carId = 1L;
        Car car = new Car(carId, "Ford", "Focus");
        when(service.getCarDetails(carId)).thenReturn(Optional.of(car));

        RestAssuredMockMvc.given()
                .when()
                .get("/api/v1/car/" + carId)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("carId", equalTo(1),
                        "maker", equalTo("Ford"),
                        "model", equalTo("Focus"));
    }

    @Test
    void givenNonExistingCarId_whenGetCar_thenReturnNotFound() {
        Long carId = 999L;
        when(service.getCarDetails(carId)).thenReturn(Optional.empty());

        RestAssuredMockMvc.given()
                .when()
                .get("/api/v1/car/" + carId)
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }
}