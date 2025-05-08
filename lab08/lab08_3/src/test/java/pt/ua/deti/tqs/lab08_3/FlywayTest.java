package pt.ua.deti.tqs.lab08_03;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
class FlywayTest {
    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer("postgres:16")
            .withUsername("user")
            .withPassword("password")
            .withDatabaseName("test");

    @Autowired
    private BookRepository bookRepository;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
    }

    @Test
    void createBook() {
        Book book = new Book();
        book.setAuthor("Testcontainers");
        book.setTitle("Testcontainers");
        book.setYear(2024);

        bookRepository.save(book);
        assertThat(bookRepository.findAll()).extracting(Book::getAuthor).contains("Testcontainers");
    }
}
