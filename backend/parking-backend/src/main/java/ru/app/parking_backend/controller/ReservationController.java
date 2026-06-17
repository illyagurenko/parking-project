package ru.app.parking_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.dto.PageResponse;
import ru.app.parking_backend.dto.ReservationDto;
import ru.app.parking_backend.entity.Reservation;
import ru.app.parking_backend.service.ReservationService;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReservationController {
    private final ReservationService service;

    @GetMapping
    public PageResponse<ReservationDto> findAll(
            @RequestParam(required = false) String carNumber,
            @RequestParam(required = false) String clientFullName,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.findAll(carNumber, clientFullName, page, size);
    }

    @GetMapping("/{id}")
    public Reservation findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public Reservation save(@Valid @RequestBody Reservation reservation) {
        return service.save(reservation);
    }

    @PutMapping("/{id}")
    public Reservation update(@PathVariable Integer id, @Valid @RequestBody Reservation reservation) {
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
