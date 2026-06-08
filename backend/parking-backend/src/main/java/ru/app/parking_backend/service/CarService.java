package ru.app.parking_backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.repository.CarRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository repository;

    // возвращает список машин или ищет их по номеру
    public List<CarDto> getAllCars(String search) {
        if (search != null && !search.isEmpty()) {
            return repository.searchByNumber(search);
        }
        return repository.findAll();
    }

    public Optional<Car> getCarById(Integer id) {
        return repository.findById(id);
    }

    public Car saveCar(Car car) {
        return repository.save(car);
    }

    public void deleteCar(Integer id) {
        repository.delete(id);
    }
}
