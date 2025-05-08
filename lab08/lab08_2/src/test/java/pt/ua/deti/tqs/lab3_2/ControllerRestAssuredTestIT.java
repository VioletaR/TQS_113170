package pt.ua.deti.tqs.lab3_2;

import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.util.UriComponentsBuilder;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@TestPropertySource(properties = "spring.jpa.hibernate.ddl-auto=create")
class ControllerRestAssuredTestIT {
    @Container
    public static MySQLContainer<?> container = new MySQLContainer<>("mysql:8")
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("test");

    @LocalServerPort
    int localPortForTestServer;

    @Autowired
    private CarRepository repository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @AfterEach
    public void resetDb() {
        repository.deleteAll();
    }

    @Test
    void whenValidInput_thenCreateBook() {
        Car peugeot = new Car("Peugeot", "3008");

        String endpoint = UriComponentsBuilder.newInstance()
                                              .scheme("http")
                                              .host("localhost")
                                              .port(localPortForTestServer)
                                              .pathSegment("api", "cars")
                                              .build()
                                              .toUriString();


        RestAssured.given().contentType("application/json").body(peugeot)
                   .post(endpoint)
                   .then().statusCode(HttpStatus.CREATED.value())
                   .body("maker", is(peugeot.getMaker()))
                   .body("model", is(peugeot.getModel()));
    }

    @Test
    void givenEmployees_whenGetEmployees_thenStatus200() {
        createTestCar("Peugeot", "3008");
        createTestCar("Citroen", "C4");

        String endpoint = UriComponentsBuilder.newInstance()
                                              .scheme("http")
                                              .host("localhost")
                                              .port(localPortForTestServer)
                                              .pathSegment("api", "cars")
                                              .build()
                                              .toUriString();

        RestAssured.given()
                   .get(endpoint)
                   .then().statusCode(HttpStatus.OK.value())
                   .body("maker", hasItems("Peugeot", "Citroen"))
                   .body("model", hasItems("3008", "C4"));
    }

    private void createTestCar(String maker, String model) {
        Car car = new Car(maker, model);
        repository.saveAndFlush(car);
    }
}

