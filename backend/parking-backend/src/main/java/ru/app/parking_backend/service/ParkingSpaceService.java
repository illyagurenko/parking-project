package ru.app.parking_backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.entity.ParkingSpace;
import ru.app.parking_backend.repository.ParkingSpaceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;

    public List<ParkingSpace> findAllParkingSpace(){
        return parkingSpaceRepository.findAllParkingSpace();
    }

    public Optional<ParkingSpace> findParkingSpaceByNumber(String number){
        return parkingSpaceRepository.findParkingSpaceByNumber(number).stream()
                .findFirst();
    }

    public ParkingSpace saveParkingSpace(ParkingSpace space){
        parkingSpaceRepository.saveParkingSpace(space);
        return space;
    }

    public void updateParkingSpaceNumber(Integer id, String newParkingSpaceNumber) {
        parkingSpaceRepository.updateParkingSpaceNumber(id, newParkingSpaceNumber);
    }

    public void deleteParkingSpace(Integer id){
        parkingSpaceRepository.deleteParkingSpace(id);
    }
}
