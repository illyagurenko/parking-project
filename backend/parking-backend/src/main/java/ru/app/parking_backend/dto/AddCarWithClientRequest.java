package ru.app.parking_backend.dto;

public record AddCarWithClientRequest (
        String numberCar,
        String fullName
) {
}
