//package ua.deti.tqs.backend;
//
//import org.springframework.boot.autoconfigure.domain.EntityScan;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.test.context.DynamicPropertyRegistry;
//import org.springframework.test.context.DynamicPropertySource;
//import org.testcontainers.containers.PostgreSQLContainer;
//import org.testcontainers.containers.wait.strategy.Wait;
//import org.testcontainers.junit.jupiter.Container;
//import org.testcontainers.junit.jupiter.Testcontainers;
//import org.testcontainers.utility.DockerImageName;
//
//import java.time.Duration;
//
//
//@Testcontainers
//@Configuration
//@EnableJpaRepositories(basePackages = "ua.deti.tqs.backend.repositories")
//@EntityScan(basePackages = "ua.deti.tqs.backend.entities")
//public class TestcontainersConfiguration {
//    @Container
//    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine")
//            .withDatabaseName("testdb")
//            .withUsername("testuser")
//            .withPassword("testpass")
//            .withStartupTimeout(Duration.ofSeconds(90))
//            .waitingFor(Wait.forListeningPort());
//    @DynamicPropertySource
//    static void configureProperties(DynamicPropertyRegistry registry) {
//        registry.add("spring.datasource.url", postgres::getJdbcUrl);
//        registry.add("spring.datasource.username", postgres::getUsername);
//        registry.add("spring.datasource.password", postgres::getPassword);
//        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
//    }
//}