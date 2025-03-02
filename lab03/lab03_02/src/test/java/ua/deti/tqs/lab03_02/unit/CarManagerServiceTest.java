package ua.deti.tqs.lab03_02.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ua.deti.tqs.lab03_02.entity.Car;
import ua.deti.tqs.lab03_02.repository.CarRepository;
import ua.deti.tqs.lab03_02.service.CarManagerService;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@ExtendWith(MockitoExtension.class)
public class CarManagerServiceTest {

    @Mock(lenient = true)
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carManagerService;

    private final Car car1 = new Car("maker1", "model1");

    @BeforeEach
    public void setUp() {
        Car car2 = new Car("maker2", "model2");
        Car car3 = new Car("maker3", "model3");

        List<Car> allCars = Arrays.asList(car1, car2, car3);

        Mockito.when(carRepository.findByCarId(car1.getCarId())).thenReturn(car1);
        Mockito.when(carRepository.findByCarId(12345L)).thenReturn(null);
        Mockito.when(carRepository.findByCarId(-99L)).thenReturn(null);
        Mockito.when(carRepository.findAll()).thenReturn(allCars);

    }

    @Test
    void whenValidId_thenCarShouldBeFound() {
        Car found = carManagerService.getCarDetails(car1.getCarId()).get();
        assert(found).equals(car1);
    }

    @Test
    void whenInValidId_thenCarShouldNotBeFound() {
        Car fromDb = carManagerService.getCarDetails(12345L).orElse(null);
        assertThat(fromDb).isNull();

    }
}