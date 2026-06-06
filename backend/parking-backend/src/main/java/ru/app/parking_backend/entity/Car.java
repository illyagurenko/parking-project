package ru.app.parking_backend.entity;


public record Car (
        Integer id,
        String numberCar,
        Integer clientId
) {
}