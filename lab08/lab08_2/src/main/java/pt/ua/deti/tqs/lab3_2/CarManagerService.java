package pt.ua.deti.tqs.lab3_2;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarManagerService {
    private final CarRepository carRepository;

    public CarManagerService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    Car save(Car car) {
        return carRepository.save(car);
    }

    List<Car> getAllCars() {
        return carRepository.findAll();
    }

    Car getCarDetails(Long carId) {
        return carRepository.findByCarId(carId);
    }
}
