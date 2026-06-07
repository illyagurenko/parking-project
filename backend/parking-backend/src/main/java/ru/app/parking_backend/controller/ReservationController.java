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
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public List<Reservation> findAllReservations(){
        return reservationService.findAllReservations();
    }

    @GetMapping("/search")
    public Optional<Reservation> findCarByNumberCarAndFullName(
            @RequestParam String carNumber,
            @RequestParam String fullName){
        return reservationService.findCarByNumberCarAndFullName(carNumber, fullName);
    }

    @PostMapping
    public void saveReservation(@RequestParam Integer carId, @RequestParam Integer parkingId){
        reservationService.saveReservation(carId, parkingId);
    }

    @PatchMapping("/{id}/release")
    public void releaseReservation(@PathVariable Integer id){
        reservationService.releaseReservation(id);
    }

    @PatchMapping("/{id}/payment")
    public void payReservation(@PathVariable Integer id){
        reservationService.payReservation(id);
    }
}
