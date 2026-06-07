package ru.app.parking_backend.entity;


import java.time.LocalDateTime;

public record Reservation (
        Integer id,
        Integer parkingId,
        Integer carId,
        boolean isPaid,
        LocalDateTime startTime,
        LocalDateTime endTime
) {
}
