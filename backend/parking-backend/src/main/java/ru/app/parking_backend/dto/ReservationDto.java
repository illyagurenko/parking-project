package ru.app.parking_backend.dto;

import java.time.OffsetDateTime;

public record ReservationDto(
        Integer id,
        Integer parkingId,
        String parkingSpaceNumber,
        Integer carId,
        String carNumber,
        String clientFullName,
        Boolean isPaid,
        OffsetDateTime startTime,
        OffsetDateTime endTime
) {
}
