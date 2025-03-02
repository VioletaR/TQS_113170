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

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(MockitoExtension.class)
public class CarManagerServiceTest {

    @Mock(lenient = true)
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carManagerService;

    @BeforeEach
    public void setUp() {
        Car car1 = new Car("maker1", "model1");
        car1.setCarId(1L);
        Car car2 = new Car("maker2", "model2");
        Car car3 = new Car("maker3", "model3");


        List<Car> allCars = Arrays.asList(car1, car2, car3);
        Mockito.when(carRepository.findByCarId(1L)).thenReturn(car1);
        Mockito.when(carRepository.findByCarId(12345L)).thenReturn(null);
        Mockito.when(carRepository.findByCarId(-99L)).thenReturn(null);
        Mockito.when(carRepository.findAll()).thenReturn(allCars);

    }

    @Test
    void whenValidId_thenCarShouldBeFound() {
        Car found = carManagerService.getCarDetails(1L).get();

        assertThat(found).isNotNull();
        assertThat(found.getMaker()).isEqualTo("maker1");
        assertThat(found.getModel()).isEqualTo("model1");

    }

    @Test
    void whenInValidId_thenCarShouldNotBeFound() {
        Car fromDb = carManagerService.getCarDetails(12345L).orElse(null);
        assertThat(fromDb).isNull();

    }

    @Test
    void given3Cars_whenGetAll_thenReturn3Records() {

        List<Car> allCars = carManagerService.getAllCars();
        assertThat(allCars).hasSize(3).extracting(Car::getMaker)
                .contains("maker1", "maker2", "maker3");
    }
}