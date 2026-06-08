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

    public List<ParkingSpace> listAvailable() {
        return repository.findAvailable();
    }

    public void create(ParkingSpace space) {
        repository.save(space);
    }

    public void update(Integer id, ParkingSpace space) {
        ParkingSpace upParkingSpace = new ParkingSpace(id, space.numberSpace());
        repository.update(space);
    }

    public void delete(Integer id) {
        repository.delete(id);
    }
}
