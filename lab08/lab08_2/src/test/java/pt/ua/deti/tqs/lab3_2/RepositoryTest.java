package pt.ua.deti.tqs.lab3_2;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class RepositoryTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    void whenFindAlexByName_thenReturnAlexEmployee() {
        // arrange a new employee and insert into db
        Car peugeot = new Car("Peugeot", "3008");
        entityManager.persistAndFlush(peugeot); //ensure data is persisted at this point

        // test the query method of interest
        Car found = carRepository.findByCarId(peugeot.getCarId());
        assertThat(found).isEqualTo(peugeot);
    }

    @Test
    void whenInvalidEmployeeName_thenReturnNull() {
        Car fromDb = carRepository.findByCarId(12345L);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenFindEmployedByExistingId_thenReturnEmployee() {
        Car car = new Car("Citroen", "C4");
        entityManager.persistAndFlush(car);

        Car fromDb = carRepository.findByCarId(car.getCarId());
        assertThat(fromDb).isNotNull();
        assertThat(fromDb.getMaker()).isEqualTo(car.getMaker());
    }

    @Test
    void whenInvalidId_thenReturnNull() {
        Car fromDb = carRepository.findByCarId(-111L);
        assertThat(fromDb).isNull();
    }

    @Test
    void givenSetOfEmployees_whenFindAll_thenReturnAllEmployees() {
        Car alex = new Car("alex", "alex@deti.com");
        Car ron = new Car("ron", "ron@deti.com");
        Car bob = new Car("bob", "bob@deti.com");

        entityManager.persist(alex);
        entityManager.persist(bob);
        entityManager.persist(ron);
        entityManager.flush();

        List<Car> allCars = carRepository.findAll();

        assertThat(allCars).hasSize(3).extracting(Car::getMaker)
                           .containsOnly(alex.getMaker(), ron.getMaker(), bob.getMaker());
    }
}
