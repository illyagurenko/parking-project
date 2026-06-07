package ru.app.parking_backend.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.dto.AddCarWithClientRequest;
import ru.app.parking_backend.dto.CarWithClientDTO;
import ru.app.parking_backend.dto.UpdateCarRequest;
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
    public List<CarWithClientDTO> findAllCar(){
        return carService.findAllCar();
    }

    @GetMapping("/{number}")
    public Optional<CarWithClientDTO> findCarByNumber(@PathVariable("number") String numberCar){
        return carService.findCarByNumber(numberCar);
    }

    @PostMapping
    public Car saveCar(@RequestBody Car car){
        return carService.saveCar(car);
    }

    @PutMapping("/{id}")
    public void updateCarNumber(@PathVariable Integer id, @RequestBody UpdateCarRequest request){
        carService.updateCarNumber(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteCar(Integer id){
        carService.deleteCar(id);
    }

    @PostMapping("/with-client")
    public ResponseEntity<CarWithClientDTO> addCarWithClient(@RequestBody AddCarWithClientRequest request) {
        CarWithClientDTO result = carService.addCarWithClient(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @GetMapping("/by-client/{clientId}")
    public List<CarWithClientDTO> findCarsByClient(@PathVariable Integer clientId) {
        return carService.findCarsByClientId(clientId);
    }
}
