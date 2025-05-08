package pt.ua.deti.tqs.lab3_2;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class ServiceTest {

    @Mock(lenient = true)
    private CarRepository carRepository;

    @InjectMocks
    private CarManagerService carManagerService;

    @BeforeEach
    public void setUp() {

        //these expectations provide an alternative to the use of the repository
        Car peugeot = new Car("Peugeot", "3008");
        peugeot.setCarId(111L);

        Car citroen = new Car("Citroen", "C4");
        Car renault = new Car("Renault", "Kangoo");

        List<Car> allCars = Arrays.asList(peugeot, citroen, renault);

        Mockito.when(carRepository.findByCarId(12345L)).thenReturn(null);
        Mockito.when(carRepository.findByCarId(peugeot.getCarId())).thenReturn(peugeot);
        Mockito.when(carRepository.findAll()).thenReturn(allCars);
        Mockito.when(carRepository.findById(-99L)).thenReturn(Optional.empty());
    }

    @Test
    void whenSearchValidId_thenCarShouldBeFound() {
        Car found = carManagerService.getCarDetails(111L);

        assertThat(found).isNotNull();
        assertThat(found.getMaker()).isEqualTo("Peugeot");
        assertThat(found.getModel()).isEqualTo("3008");
    }

    @Test
    void whenSearchInvalidName_thenEmployeeShouldNotBeFound() {
        Car fromDb = carManagerService.getCarDetails(12345L);
        assertThat(fromDb).isNull();

        verifyFindByCarIdIsCalledOnce(12345L);
    }

    @Test
    void given3Employees_whengetAll_thenReturn3Records() {
        Car peugeot = new Car("Peugeot", "3008");
        peugeot.setCarId(111L);

        Car citroen = new Car("Citroen", "C4");
        Car renault = new Car("Renault", "Kangoo");

        List<Car> allCars = carManagerService.getAllCars();
        verifyFindAllCarsIsCalledOnce();
        assertThat(allCars).hasSize(3).extracting(Car::getMaker)
                           .contains(peugeot.getMaker(), citroen.getMaker(), renault.getMaker());
    }

    private void verifyFindByCarIdIsCalledOnce(Long id) {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findByCarId(id);
    }

    private void verifyFindAllCarsIsCalledOnce() {
        Mockito.verify(carRepository, VerificationModeFactory.times(1)).findAll();
    }
}
