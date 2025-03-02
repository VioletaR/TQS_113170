package ua.deti.tqs.lab03_02.unit;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import ua.deti.tqs.lab03_02.entity.Car;
import ua.deti.tqs.lab03_02.repository.CarRepository;

import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CarRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    void whenFindCarById_thenReturnCar() {
        Car car = new Car("maker", "model");
        entityManager.persistAndFlush(car);

        Car found = carRepository.findByCarId(car.getCarId());
        assertThat(found).isEqualTo(car);
    }

    @Test
    void whenInvalidCarId_thenReturnNull() {
        Car fromDb = carRepository.findByCarId(-1L);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindAllCars_thenReturnCars() {
        Car car1 = new Car("maker1", "model1");
        Car car2 = new Car("maker2", "model2");

        entityManager.persistAndFlush(car1);
        entityManager.persistAndFlush(car2);

        List<Car> allCars = carRepository.findAll();
        assertThat(allCars).hasSize(2).extracting(Car::getMaker)
                .containsOnly(car1.getMaker(), car2.getMaker());

    }


}
