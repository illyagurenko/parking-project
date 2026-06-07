package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;
import ru.app.parking_backend.repository.ReservationRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<ReservationDto> listActive(String reserv) {
        if (reserv != null && !reserv.trim().isEmpty()) {
            return reservationRepository.searchActive(reserv);
        }
        return reservationRepository.findAllActive();
    }

    public void create(Reservation reservation) {
        reservationRepository.create(reservation);
    }

    public void updatePayment(Integer id, boolean isPaid) {
        reservationRepository.updatePayment(id, isPaid);
    }

    public void releaseSpace(Integer id) {
        reservationRepository.releaseSpace(id);
    }
}
