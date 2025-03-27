package ua.deti.tqs.lab06_02.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.lab06_02.entity.Book;

import java.util.Optional;

public interface BookRepo extends JpaRepository<Book, Long> {
    Optional<Book> findByName(String name);
}