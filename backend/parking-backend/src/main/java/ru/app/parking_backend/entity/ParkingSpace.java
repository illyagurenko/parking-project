package ru.app.parking_backend.entity;

import jakarta.validation.constraints.NotBlank;

public record ParkingSpace(
        Integer id,
        @NotBlank(message = "Номер места обязателен") String numberSpace
)  {
}