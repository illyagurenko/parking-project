package ru.app.parking_backend.dto;

public record CarDto (
        Integer id,
        String numberCar,
        Integer clientId,
        String clientName
){
}
