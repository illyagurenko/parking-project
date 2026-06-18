package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.app.parking_backend.dto.CarDto;
import ru.app.parking_backend.dto.PageResponse;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.exception.ResourceNotFoundException;
import ru.app.parking_backend.repository.CarRepository;
import ru.app.parking_backend.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class CarService {
    private final CarRepository repository;
    private final ReservationRepository reservationRepository;

    //отправляет нужный кусок бд
    public PageResponse<CarDto> findAll(String search, int page, int size) {
        // параметры пагинации
        if (page < 0) page = 0;
        if (size < 10) size = 10;
        if (size > 100) size = 100;

        // расчет сколько записей пропустить
        int offset = page * size;

        List<CarDto> content;
        long totalElements;

        //с фильтром
        if (search != null && !search.trim().isEmpty()) {
            String searchPattern = search.trim();
            content = repository.searchByNumberPaginated(searchPattern, size, offset);
            totalElements = repository.countByNumber(searchPattern);
        } else {
            content = repository.findAllPaginated(size, offset);
            totalElements = repository.countAll();
        }

        //всего стр
        int totalPages = (int) Math.ceil((double) totalElements / size);

        return new PageResponse<>(
                content,
                totalElements,
                totalPages,
                page,
                size
        );
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
        if (reservationRepository.hasActiveCar(id)) {
            throw new IllegalStateException("Невозможно удалить машину: она в данный момент находится на парковке");
        }
        repository.delete(id);
    }
}
