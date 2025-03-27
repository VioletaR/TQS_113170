package ua.deti.tqs.lab06_01.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.lab06_01.entity.Book;

public interface BookRepo extends JpaRepository<Book, Long> {
}