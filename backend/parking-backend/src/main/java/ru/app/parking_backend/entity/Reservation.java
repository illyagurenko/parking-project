package ru.app.parking_backend.entity;

import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;

public record Reservation (
        Integer id,
        @NotNull(message = "Парковочное место обязательно") Integer parkingId,
        @NotNull(message = "Машина обязательна") Integer carId,
        Boolean isPaid,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {
}
