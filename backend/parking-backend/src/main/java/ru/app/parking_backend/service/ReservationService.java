package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.app.parking_backend.dto.PageResponse;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;
import ru.app.parking_backend.exception.ResourceNotFoundException;
import ru.app.parking_backend.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationService {

    private final ReservationRepository repository;

    //отправляет нужный кусок бд
    public PageResponse<ReservationDto> findAll(String carNumber, String clientFullName, int page, int size) {
        // параметры пагинации
        if (page < 0) page = 0;
        if (size < 10) size = 10;
        if (size > 100) size = 100;

        // расчет сколько записей пропустить
        int offset = page * size;

        List<ReservationDto> content;
        long totalElements;

        boolean hasCarFilter = carNumber != null && !carNumber.trim().isEmpty();
        boolean hasClientFilter = clientFullName != null && !clientFullName.trim().isEmpty();

        //с фильтрами
        if (hasCarFilter || hasClientFilter) {
            String cleanCar = hasCarFilter ? carNumber.trim() : null;
            String cleanClient = hasClientFilter ? clientFullName.trim() : null;

            content = repository.searchPaginated(cleanCar, cleanClient, size, offset);
            totalElements = repository.countSearch(cleanCar, cleanClient);
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

    public Reservation findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронь с id " + id + " не найдена"));
    }

    public Reservation save(Reservation reservation) {
        if (reservation.id() != null && !repository.existById(reservation.id())) {
            throw new ResourceNotFoundException("Бронь с id " + reservation.id() + " не найдена");
        }
        if (reservation.id() == null) {

            if (repository.hasActiveCar(reservation.carId())) {
                throw new IllegalStateException("Этот автомобиль уже находится на парковке!");
            }

            if (repository.hasActiveSpace(reservation.parkingId())) {
                throw new IllegalStateException("Это парковочное место уже занято другим автомобилем!");
            }
        }
        return repository.save(reservation);
    }

    public void delete(Integer id) {
        if (!repository.existById(id)) {
            throw new ResourceNotFoundException("Бронь с id " + id + " не найдена");
        }
        repository.delete(id);
    }
}
