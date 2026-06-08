package ru.app.parking_backend.entity;


import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public record Reservation (
        Integer id,
        Integer parkingId,
        Integer carId,
        Boolean isPaid,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {
}
