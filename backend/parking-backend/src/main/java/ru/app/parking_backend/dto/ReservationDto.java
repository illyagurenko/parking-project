package ru.app.parking_backend.dto;

import java.time.OffsetDateTime;

public record ReservationDto(
        Integer id,
        Integer parkingId,
        String numberSpace,
        Integer carId,
        String numberCar,
        Integer clientId,
        String fullName,
        Boolean isPaid,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {
}
