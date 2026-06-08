package ru.app.parking_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.service.CarService;


import java.util.List;


@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CarController {
    private final CarService service;

    @GetMapping
    public List<CarDto> getAll(@RequestParam(required = false) String search) {
        return service.getAllCars(search);
    }

    @GetMapping("/{id}")
    public Car getById(@PathVariable Integer id) {
        return service.getCarById(id).orElse(null);
    }

    @PostMapping
    public Car create(@RequestBody Car car) {
        return service.saveCar(car);
    }

    @PutMapping("/{id}")
    public Car update(@PathVariable Integer id, @RequestBody Car car) {
        Car updatedCar = new Car(id, car.numberCar(), car.clientId());
        return service.saveCar(updatedCar);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.deleteCar(id);
    }
}
