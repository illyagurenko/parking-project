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

    private final ParkingSpaceRepository repository;

    public List<ParkingSpace> findAll() {
        return repository.findAll();
    }

    public Optional<ParkingSpace> findById(Integer id) {
        return repository.findById(id);
    }

    public ParkingSpace save(ParkingSpace parkingSpace) {
        return repository.save(parkingSpace);
    }

    public void delete(Integer id) {
        repository.delete(id);
    }
}
