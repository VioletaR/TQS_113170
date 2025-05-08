package pt.ua.deti.tqs.lab3_2;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByCarId(Long id);

    @Override
    @NonNull
    List<Car> findAll();
}
