package ru.app.parking_backend.dto;

import java.time.OffsetDateTime;

public record ReservationDto(
        Integer id,
        Integer parkingId,
        String parkingNumber,
        Integer carId,
        String carNumber,
        Integer clientId,
        String clientFullName,
        Boolean isPaid,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {
}
