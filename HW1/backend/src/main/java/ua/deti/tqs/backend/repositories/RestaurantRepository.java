package ua.deti.tqs.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.deti.tqs.backend.entities.Restaurant;

import java.util.Optional;

public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {
    Optional<Restaurant> findByName(String name);
}
