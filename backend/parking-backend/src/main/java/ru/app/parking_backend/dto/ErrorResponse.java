package ru.app.parking_backend.dto;

public record ErrorResponse(
         int status,
         String message
) {
}
