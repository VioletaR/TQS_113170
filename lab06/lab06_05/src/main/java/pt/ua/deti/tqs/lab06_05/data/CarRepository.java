package pt.ua.deti.tqs.lab06_05.data;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.ua.deti.tqs.lab06_05.model.Car;

public interface CarRepository extends JpaRepository<Car, Long> {
    Car findByCarId(Long carId);

//    List<Car> findAll();
}
