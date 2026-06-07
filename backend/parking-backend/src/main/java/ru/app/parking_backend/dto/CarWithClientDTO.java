package ru.app.parking_backend.dto;

public record CarWithClientDTO(
         Integer id,
         String numberCar,
         Integer clientId,
         String clientFullName
) {
}
