package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.app.parking_backend.dto.PageResponse;
import ru.app.parking_backend.entity.ParkingSpace;
import ru.app.parking_backend.exception.ResourceNotFoundException;
import ru.app.parking_backend.repository.ParkingSpaceRepository;
import ru.app.parking_backend.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ParkingSpaceService {

    private final ParkingSpaceRepository repository;
    private final ReservationRepository reservationRepository;

    //отправляет нужный кусок бд
    public PageResponse<ParkingSpace> findAll(String search, int page, int size) {
        // параметры пагинации
        if (page < 0) page = 0;
        if (size < 10) size = 10;
        if (size > 100) size = 100;

        // расчет сколько записей пропустить
        int offset = page * size;

        List<ParkingSpace> content;
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

    public ParkingSpace findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Парковочное место с id " + id + " не найдено"));
    }

    public ParkingSpace save(ParkingSpace parkingSpace) {
        if (parkingSpace.id() != null && !repository.existById(parkingSpace.id())) {
            throw new ResourceNotFoundException("Парковочное место с id " + parkingSpace.id() + " не найдено");
        }
        return repository.save(parkingSpace);
    }

    public void delete(Integer id) {
        if (!repository.existById(id)) {
            throw new ResourceNotFoundException("Парковочное место с id " + id + " не найдено");
        }
        if (reservationRepository.hasActiveSpace(id)) {
            throw new IllegalStateException("Невозможно удалить парковочное место: оно в данный момент занято автомобилем");
        }
        repository.delete(id);
    }
}

