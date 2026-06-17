package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;
import ru.app.parking_backend.exception.ResourceNotFoundException;
import ru.app.parking_backend.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository repository;

    // функция возвращает все бронирования или ищет их по фильтрам
    public List<ReservationDto> findAll(String carNumber, String clientFullName) {
        if ((carNumber != null && !carNumber.isEmpty()) || (clientFullName != null && !clientFullName.isEmpty())) {
            return repository.search(carNumber, clientFullName);
        }
        return repository.findAll();
    }

    public Reservation findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронь с id " + id + " не найдена"));
    }

    public Reservation save(Reservation reservation) {
        if (reservation.id() != null && !repository.existById(reservation.id())) {
            throw new ResourceNotFoundException("Бронь с id " + reservation.id() + " не найдена");
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
