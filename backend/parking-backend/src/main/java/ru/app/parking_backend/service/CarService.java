package ru.app.parking_backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.exception.ResourceNotFoundException;
import ru.app.parking_backend.repository.CarRepository;
import ru.app.parking_backend.repository.ReservationRepository;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository repository;
    private final ReservationRepository reservationRepository;

    // возвращает список машин или ищет их по номеру
    public List<CarDto> findAll(String search) {
        if (search != null && !search.isEmpty()) {
            return repository.searchByNumber(search);
        }
        return repository.findAll();
    }

    public Car findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Машина с id " + id + " не найдена"));
    }

    public Car save(Car car) {
        if (car.id() != null && !repository.existById(car.id())) {
            throw new ResourceNotFoundException("Машина с id " + car.id() + " не найдена");
        }
        return repository.save(car);
    }

    public void delete(Integer id) {
        if (!repository.existById(id)) {
            throw new ResourceNotFoundException("Машина с id " + id + " не найдена");
        }
        if(reservationRepository.hasActiveCar(id)){
            throw new IllegalStateException("Невозможно удалить машину: она в данный момент находится на парковке");
        }
        repository.delete(id);
    }
}
