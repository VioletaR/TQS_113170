package pt.ua.deti.tqs.lab3_2;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CarController.class)
class ControllerRestAssuredTest {
    @MockBean
    CarManagerService carManagerService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void givenCars_whenGetCars_thenStatus200() {
        Car car = new Car("Peugeot", "3008");

        RestAssuredMockMvc
                .given().mockMvc(mockMvc).contentType("application/json").body(car)
                .when().post("/api/cars")
                .then().statusCode(HttpStatus.CREATED.value());
    }
}
