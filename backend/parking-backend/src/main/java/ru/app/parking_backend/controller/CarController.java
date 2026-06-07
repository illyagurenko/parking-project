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
    private final CarService carService;

    @GetMapping
    public List<CarDto> list(@RequestParam(required = false) String number) {
        return carService.findAllCarWithClient(number);
    }

    @PostMapping
    public void create(@RequestBody Car car) {
        carService.create(car);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody Car car) {
        carService.update(id, car);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        carService.delete(id);
    }
}
