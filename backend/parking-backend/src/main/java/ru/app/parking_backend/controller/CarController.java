package ru.app.parking_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.dto.PageResponse;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.service.CarService;

@RestController
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CarController {
    private final CarService service;

    @GetMapping
    public PageResponse<CarDto> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.findAll(search, page, size);
    }

    @GetMapping("/{id}")
    public Car findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public Car save(@Valid @RequestBody Car car) {
        return service.save(car);
    }

    @PutMapping("/{id}")
    public Car update(@PathVariable Integer id, @Valid @RequestBody Car car) {
        Car updatedCar = new Car(id, car.numberCar(), car.clientId());
        return service.save(updatedCar);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
