package pt.ua.deti.tqs.lab06_05.services;


import org.springframework.stereotype.Service;
import pt.ua.deti.tqs.lab06_05.data.CarRepository;
import pt.ua.deti.tqs.lab06_05.model.Car;

import java.util.List;
import java.util.Optional;

@Service
public class CarManagerService {

    final
    CarRepository carRepository;

    public CarManagerService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }


    public Car save(Car oneCar) {
        return carRepository.save(oneCar);
    }

    public List<Car> getAllCars() {

        return carRepository.findAll();
    }

    public Optional<Car> getCarDetails(Long carId) {
        return Optional.ofNullable(carRepository.findByCarId(carId));
    }
}

