package ru.app.parking_backend.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record ParkingSpace(
        Integer id,
        @NotBlank(message = "Номер места обязателен")
        @Pattern(
                regexp = "^N_.+$",
                message = "Номер парковочного места должен начинаться с 'N_'"
        )
        String numberSpace
)  {
}