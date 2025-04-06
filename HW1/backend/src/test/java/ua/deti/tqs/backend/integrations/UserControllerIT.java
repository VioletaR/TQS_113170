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
import ua.deti.tqs.backend.entities.User;
import ua.deti.tqs.backend.entities.utils.UserRole;
import ua.deti.tqs.backend.repositories.UserRepository;
import ua.deti.tqs.backend.utils.Constants;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserControllerIT {
    @Container
    private static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16")
            .withDatabaseName("testdb")
            .withUsername("user")
            .withPassword("password");

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        RestAssured.port = port;
        userRepository.deleteAll();
        // Create a default user for authentication
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin");
        admin.setRole(UserRole.STAFF);
        userRepository.save(admin);
    }

    private User createUser(String username, String password, UserRole role) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setRole(role);
        return userRepository.save(user);
    }

    @Test
    void whenPostValidUser_thenCreateUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpass");

        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(user)
                .when()
                .post(Constants.API_PATH_V1 + "users")
                .then()
                .statusCode(HttpStatus.CREATED.value())
                .body("username", equalTo("testuser"))
                .body("role", equalTo("USER"));

        assertThat(userRepository.count()).isEqualTo(2); // admin + new user
    }

    @Test
    void whenPostDuplicateUsername_thenReturnBadRequest() {
        createUser("existing", "password", UserRole.USER);
        User duplicate = new User();
        duplicate.setUsername("existing");
        duplicate.setPassword("password");

        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(duplicate)
                .when()
                .post(Constants.API_PATH_V1 + "users")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenPostUserWithMissingFields_thenReturnBadRequest() {
        User invalidUser = new User();
        invalidUser.setUsername("");

        given()
                .auth().preemptive().basic("admin", "admin")
                .contentType(ContentType.JSON)
                .body(invalidUser)
                .when()
                .post(Constants.API_PATH_V1 + "users")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenGetOwnUserById_thenReturnUser() {
        User user = createUser("testuser", "testpass", UserRole.USER);

        given()
                .auth().preemptive().basic("testuser", "testpass")
                .pathParam("id", user.getId())
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "users/{id}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("testuser"));
    }

    @Test
    void whenGetOtherUserById_thenReturnNotFound() {
        User user1 = createUser("user1", "pass1", UserRole.USER);
        User user2 = createUser("user2", "pass2", UserRole.USER);

        given()
                .auth().preemptive().basic("user1", "pass1")
                .pathParam("id", user2.getId())
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "users/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenGetOwnUserByName_thenReturnUser() {
        createUser("testuser", "testpass", UserRole.USER);

        given()
                .auth().preemptive().basic("testuser", "testpass")
                .pathParam("name", "testuser")
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "users/name/{name}")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("testuser"));
    }

    @Test
    void whenGetOtherUserByName_thenReturnNotFound() {
        createUser("user1", "pass1", UserRole.USER);
        createUser("user2", "pass2", UserRole.USER);

        given()
                .auth().preemptive().basic("user1", "pass1")
                .pathParam("name", "user2")
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "users/name/{name}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenUpdateOwnUser_thenReturnUpdatedUser() {
        User user = createUser("olduser", "oldpass", UserRole.USER);
        User updateData = new User();
        updateData.setId(user.getId());
        updateData.setUsername("newuser");
        updateData.setPassword("newpass");

        given()
                .auth().preemptive().basic("olduser", "oldpass")
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_PRIVATE_V1 + "users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("username", equalTo("newuser"));

        User updated = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getUsername()).isEqualTo("newuser");
        assertThat(updated.getPassword()).isEqualTo("newpass");
    }

    @Test
    void whenUpdateOtherUser_thenReturnBadRequest() {
        User user1 = createUser("user1", "pass1", UserRole.USER);
        User user2 = createUser("user2", "pass2", UserRole.USER);
        User updateData = new User();
        updateData.setId(user2.getId());
        updateData.setUsername("hacked");

        given()
                .auth().preemptive().basic("user1", "pass1")
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_PRIVATE_V1 + "users")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenUpdateUserWithNoChanges_thenReturnBadRequest() {
        User user = createUser("nochanges", "pass", UserRole.USER);
        User updateData = new User();
        updateData.setId(user.getId());

        given()
                .auth().preemptive().basic("nochanges", "pass")
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_PRIVATE_V1 + "users")
                .then()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    void whenDeleteOwnUser_thenReturnNoContent() {
        User user = createUser("deleteme", "pass", UserRole.USER);

        given()
                .auth().preemptive().basic("deleteme", "pass")
                .pathParam("id", user.getId())
                .when()
                .delete(Constants.API_PATH_PRIVATE_V1 + "users/{id}")
                .then()
                .statusCode(HttpStatus.NO_CONTENT.value());

        assertThat(userRepository.existsById(user.getId())).isFalse();
    }

    @Test
    void whenDeleteOtherUser_thenReturnNotFound() {
        User user1 = createUser("user1", "pass1", UserRole.USER);
        User user2 = createUser("user2", "pass2", UserRole.USER);

        given()
                .auth().preemptive().basic("user1", "pass1")
                .pathParam("id", user2.getId())
                .when()
                .delete(Constants.API_PATH_PRIVATE_V1 + "users/{id}")
                .then()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void whenUnauthenticatedRequest_thenReturnUnauthorized() {
        given()
                .contentType(ContentType.JSON)
                .when()
                .get(Constants.API_PATH_PRIVATE_V1 + "users/1")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @Test
    void whenChangeOwnRole_thenReturnUpdatedUser() {
        User user = createUser("roleuser", "pass", UserRole.USER);
        User updateData = new User();
        updateData.setId(user.getId());
        updateData.setRole(UserRole.STAFF);

        given()
                .auth().preemptive().basic("roleuser", "pass")
                .contentType(ContentType.JSON)
                .body(updateData)
                .when()
                .put(Constants.API_PATH_PRIVATE_V1 + "users")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("role", equalTo("STAFF"));

        User updated = userRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getRole()).isEqualTo(UserRole.STAFF);
    }
}