package ru.app.parking_backend.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record Car (
        Integer id,
        @NotBlank(message = "Номер машины обязателен") String numberCar,
        @NotNull(message = "Владелец обязателен") Integer clientId
) {

}