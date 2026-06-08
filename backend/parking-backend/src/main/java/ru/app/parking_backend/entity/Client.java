package ru.app.parking_backend.entity;

import jakarta.validation.constraints.NotBlank;

public record Client(
        Integer id,
        @NotBlank(message = "ФИО не может быть пустым") String fullName
){}




