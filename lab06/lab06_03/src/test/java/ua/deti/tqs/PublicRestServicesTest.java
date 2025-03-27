package ua.deti.tqs;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class PublicRestServicesTest {
    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com/todos/";
    }

    @Test
    public void testListAllToDos() {
        given().when().get().then().statusCode(200);
    }

    @Test
    public void testToDo4() {
        given().when().get("4").then().body("title", equalTo("et porro tempora"));
    }

    @Test
    public void testToDos198and199() {
        given().when().get().then().body("id[197]", equalTo(198)).body("id[198]", equalTo(199));
    }

    @Test
    public void testSpeed() {
        given().when().get().then().time(lessThan(2000L));
    }

}
