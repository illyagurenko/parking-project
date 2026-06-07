package ru.app.parking_backend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;
import ru.app.parking_backend.service.ReservationService;

import java.util.List;


@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ReservationController {
    private final ReservationService reservationService;

    @GetMapping
    public List<ReservationDto> listActive(@RequestParam(required = false) String reserv) {
        return reservationService.listActive(reserv);
    }

    // Принимает сущность Reservation для записи в БД (достаточно прислать parkingId, carId, isPaid)
    @PostMapping
    public void create(@RequestBody Reservation reservation) {
        reservationService.create(reservation);
    }

    @PutMapping("/{id}/pay")
    public void updatePayment(@PathVariable Integer id, @RequestParam boolean isPaid) {
        reservationService.updatePayment(id, isPaid);
    }

    @PutMapping("/{id}/release")
    public void release(@PathVariable Integer id) {
        reservationService.releaseSpace(id);
    }
}
