package ru.app.parking_backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.app.parking_backend.dto.AddCarWithClientRequest;
import ru.app.parking_backend.dto.CarWithClientDTO;
import ru.app.parking_backend.dto.UpdateCarRequest;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.repository.CarRepository;
import ru.app.parking_backend.repository.ClientRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    private final ClientRepository clientRepository;

    public List<CarWithClientDTO> findAllCar(){
        return carRepository.findAllWithClient();
    }

    public Optional<CarWithClientDTO> findCarByNumber(String numberCar){
        return carRepository.findByNumberCarWithClient(numberCar).stream()
                .findFirst();
    }

    public Car saveCar(Car car){
        carRepository.saveCar(car);
        return car;
    }

    public void updateCarNumber(Integer id, UpdateCarRequest request) {
        carRepository.updateCarNumber(id, request.numberCar());
    }

    public void deleteCar(Integer id){
        carRepository.deleteCar(id);
    }

    @Transactional
    public CarWithClientDTO addCarWithClient(AddCarWithClientRequest request) {
        // 1. Ищем клиента (регистронезависимо)
        Client client = clientRepository.findClientByFullName(request.fullName()).getFirst();

        // 2. Создаём машину
        Car car = carRepository.saveCar(carRepository.findCarByNumber(request.numberCar()).getFirst());

        return new CarWithClientDTO(
                car.id(),
                car.numberCar(),
                client.id(),
                client.fullName()
        );
    }

    public List<CarWithClientDTO> findCarsByClientId(Integer clientId) {
        return carRepository.findByClientId(clientId);
    }
}
