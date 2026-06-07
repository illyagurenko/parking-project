package ru.app.parking_backend.service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.entity.ParkingSpace;
import ru.app.parking_backend.repository.ParkingSpaceRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;

    public List<ParkingSpace> findAll() {
        return parkingSpaceRepository.findAll();
    }

    public List<ParkingSpace> listAvailable() {
        return parkingSpaceRepository.findAvailable();
    }

    public void create(ParkingSpace space) {
        parkingSpaceRepository.save(space);
    }

    public void update(Integer id, ParkingSpace space) {
        ParkingSpace upParkingSpace = new ParkingSpace(id, space.numberSpace());
        parkingSpaceRepository.update(space);
    }

    public void delete(Integer id) {
        parkingSpaceRepository.delete(id);
    }
}
