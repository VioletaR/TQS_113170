package ua.deti.tqs.lab06_04.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;
import ua.deti.tqs.lab06_04.entity.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByCarId(Long id);

    @Override
    @NonNull
    List<Car> findAll();

}
