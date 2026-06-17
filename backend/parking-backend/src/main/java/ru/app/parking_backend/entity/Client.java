package ru.app.parking_backend.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record Client(
        Integer id,
        @NotBlank(message = "ФИО не может быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
        String fullName
) {
}




