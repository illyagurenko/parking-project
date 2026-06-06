package ru.app.parking_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.service.CarService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CarController {
    private final CarService carService;

    @GetMapping
    public List<Car> findAllCar(){
        return carService.findAllCar();
    }

    @GetMapping("/{number_car}")
    public Optional<Car> findCarByNumber(@PathVariable String numberCar){
        return carService.findCarByNumber(numberCar);
    }

    @PostMapping
    public Car saveCar(@RequestBody Car car){
        return carService.saveCar(car);
    }
    @DeleteMapping("/{id}")
    public void deleteCar(Integer id){
        carService.deleteCar(id);
    }
}
