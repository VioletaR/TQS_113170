package ua.deti.tqs.lab06_04.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ua.deti.tqs.lab06_04.entity.Car;
import ua.deti.tqs.lab06_04.service.CarManagerService;

import java.util.List;

@RestController
@RequestMapping("/api/v1/car")
public class CarController {
    @Autowired
    private CarManagerService carManagerService;

    @PostMapping
    public ResponseEntity<Car> createCar(@RequestBody Car car) {
        Car response = carManagerService.save(car);
        if (response == null) return ResponseEntity.badRequest().build();

        return  new ResponseEntity<Car>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public List<Car> getAllCars() {
        return carManagerService.getAllCars();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") Long id) {
        Car response = carManagerService.getCarDetails(id).orElse(null);

        if (response == null) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(response);
    }
}
