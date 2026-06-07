package ru.app.parking_backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.repository.CarRepository;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;

    public List<CarDto> findAllCarWithClient(String number) {
        if (number != null && !number.trim().isEmpty()) {
            return carRepository.searchCarByNumber(number);
        }
        return carRepository.findAllCarWithClient();
    }

    public void create(Car car) {
        carRepository.save(car);
    }

    public void update(Integer id, Car car) {
        Car newCar = new Car(id, car.numberCar(), car.clientId());
        carRepository.update(newCar);
    }

    public void delete(Integer id) {
        carRepository.delete(id);
    }
}
