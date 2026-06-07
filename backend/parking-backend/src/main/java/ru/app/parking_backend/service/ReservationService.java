package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.entity.Reservation;
import ru.app.parking_backend.repository.ReservationRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

    public List<Reservation> findAllReservations(){
        return reservationRepository.findAllReservations();
    }

    public Optional<Reservation> findCarByNumberCarAndFullName(String carNumber, String fullName){
        return reservationRepository.findCarByNumberCarAndFullName(carNumber, fullName).stream()
                .findFirst();
    }

    public void saveReservation(Integer carId, Integer parkingId){
        reservationRepository.saveReservation(carId, parkingId);
    }

    public void releaseReservation(Integer id){
        reservationRepository.releaseReservation(id);
    }

    public void payReservation(Integer id){
        reservationRepository.payReservation(id);
    }
}
