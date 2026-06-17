package ru.app.parking_backend.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record Car (
        Integer id,
        @NotBlank(message = "Номер машины обязателен")
        @Pattern(regexp = "^[A-Z0-9- ]+$", message = "Номер машины может содержать только латинские буквы, цифры")
        @Size(min = 7, max = 9)
        String numberCar,
        @NotNull(message = "Владелец обязателен") Integer clientId
) {

}