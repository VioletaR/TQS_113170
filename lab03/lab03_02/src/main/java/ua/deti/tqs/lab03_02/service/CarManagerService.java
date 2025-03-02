package ua.deti.tqs.lab03_02.service;

import org.springframework.stereotype.Service;
import ua.deti.tqs.lab03_02.entity.Car;
import ua.deti.tqs.lab03_02.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CarManagerService {
    private final CarRepository carRepository;

    public CarManagerService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    public Car save(Car car) {
        if (
            car == null || carRepository.findByCarId(car.getCarId()) != null
        ) return null;

        carRepository.save(car);
        return car;
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> getCarDetails(Long id) {
        return Optional.ofNullable(carRepository.findByCarId(id));

    }


}
