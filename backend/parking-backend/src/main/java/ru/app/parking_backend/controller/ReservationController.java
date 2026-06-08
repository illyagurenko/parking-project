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
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationService service;

    // функция обрабатывает гет запрос на получение списка бронирований с поиском
    @GetMapping
    public List<ReservationDto> findAll(@RequestParam(required = false) String carNumber,
                                       @RequestParam(required = false) String clientFullName) {
        return service.findAll(carNumber, clientFullName);
    }

    @GetMapping("/{id}")
    public Reservation findById(@PathVariable Integer id) {
        return service.findById(id).orElse(null);
    }

    @PostMapping
    public Reservation save(@RequestBody Reservation reservation) {
        return service.save(reservation);
    }

    @PutMapping("/{id}")
    public Reservation update(@PathVariable Integer id, @RequestBody Reservation reservation) {
        Reservation updatedReservation = new Reservation(
                id,
                reservation.parkingId(),
                reservation.carId(),
                reservation.isPaid(),
                reservation.startTime(),
                reservation.endTime()
        );
        return service.save(updatedReservation);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
