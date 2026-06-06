package ru.app.parking_backend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.entity.Reservation;
import ru.app.parking_backend.service.ReservationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public List<Reservation> findAllReservations(){
        return reservationService.findAllReservations();
    }
    @GetMapping("/{number_car}/{full_name}")
    public Optional<Reservation> findCarByNumberCarAndFullName(@PathVariable String carNumber,@PathVariable String fullName){
        return reservationService.findCarByNumberCarAndFullName(carNumber, fullName);
    }

    @PostMapping
    public void saveReservation(Integer carId, Integer parkingSpaceId){
        reservationService.saveReservation(carId, parkingSpaceId);
    }
    @PatchMapping("/{id}/release")
    public void releaseReservation(Integer id){
        reservationService.releaseReservation(id);
    }
    @PatchMapping("/{id}/payment")
    public void payReservation(Integer id){
        reservationService.payReservation(id);
    }

}
