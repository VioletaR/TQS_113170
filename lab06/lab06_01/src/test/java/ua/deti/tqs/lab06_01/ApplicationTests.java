package ua.deti.tqs.lab06_01;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ua.deti.tqs.lab06_01.entity.Book;
import ua.deti.tqs.lab06_01.repo.BookRepo;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ApplicationTests {

    @Container
    public static PostgreSQLContainer<?> container = new PostgreSQLContainer<>("postgres:latest")
            .withUsername("root")
            .withPassword("root")
            .withDatabaseName("test");

    @Autowired
    private BookRepo bookRepo;

    private static Long bookId;

    @DynamicPropertySource
    static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.password", container::getPassword);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
    }

    @Test
    @Order(1)
    void testInsertBook() {
        Book book = new Book();
        book.setName("Testcontainers");
        Book savedBook = bookRepo.save(book);

        assertNotNull(savedBook.getId(), "Book ID should not be null after saving");
        bookId = savedBook.getId();
    }

    @Test
    @Order(2)
    void testRetrieveBook() {
        Optional<Book> retrievedBook = bookRepo.findById(bookId);

        assertTrue(retrievedBook.isPresent(), "Book should be found in the database");
        assertEquals("Testcontainers", retrievedBook.get().getName(), "Book name should match the inserted value");
    }

    @Test
    @Order(3)
    void testUpdateBook() {
        Optional<Book> retrievedBook = bookRepo.findById(bookId);
        assertTrue(retrievedBook.isPresent(), "Book should exist before update");

        Book book = retrievedBook.get();
        book.setName("Updated Book Name");
        bookRepo.save(book);

        Optional<Book> updatedBook = bookRepo.findById(bookId);
        assertTrue(updatedBook.isPresent(), "Updated book should still exist");
        assertEquals("Updated Book Name", updatedBook.get().getName(), "Book name should be updated");
    }

    @Test
    @Order(4)
    void testRetrieveUpdatedBook() {
        Optional<Book> updatedBook = bookRepo.findById(bookId);

        assertTrue(updatedBook.isPresent(), "Updated book should be retrievable");
        assertEquals("Updated Book Name", updatedBook.get().getName(), "Updated name should be correct");
    }
}
