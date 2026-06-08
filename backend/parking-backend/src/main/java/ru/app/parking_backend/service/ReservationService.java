package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;
import ru.app.parking_backend.repository.ReservationRepository;

import java.util.List;
import java.util.Optional;

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

    public Optional<Reservation> findById(Integer id) {
        return repository.findById(id);
    }

    public Reservation save(Reservation reservation) {
        return repository.save(reservation);
    }

    public void delete(Integer id) {
        repository.delete(id);
    }
}
