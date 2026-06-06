package ru.app.parking_backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.repository.CarRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public List<Car> findAllCar(){
        return carRepository.findAllCar();
    }

    public Optional<Car> findCarByNumber(String numberCar){
        return carRepository.findCarByNumber(numberCar).stream()
                .findFirst();
    }

    public Car saveCar(Car car){
        carRepository.saveCar(car);
        return car;
    }

    public void deleteCar(Integer id){
        carRepository.deleteCar(id);
    }
}
