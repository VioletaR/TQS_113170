package pt.ua.deti.tqs;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

class AppTest {
    @Test
    void whenGetAllTodos_ThenStatus200() {
        get("https://jsonplaceholder.typicode.com/todos").then().statusCode(HttpStatus.SC_OK);
    }

    @Test
    void whenGetTodoFour_ThenCheckTitle() {
        get("https://jsonplaceholder.typicode.com/todos/4").then().body("title", equalTo("et porro tempora"));
    }

    @Test
    void whenGetAllTodos_ThenExists198And199() {
        get("https://jsonplaceholder.typicode.com/todos").then().body("id", hasItems(198, 199));
    }

    @Test
    void whenGetAllTodos_ThenResultsComeInLessThanTwoSeconds() {
        get("https://jsonplaceholder.typicode.com/todos").then().time(lessThan(2000L));
    }
}
