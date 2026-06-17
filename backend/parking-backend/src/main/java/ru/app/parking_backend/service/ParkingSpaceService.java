package ru.app.parking_backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.entity.ParkingSpace;
import ru.app.parking_backend.exception.ResourceNotFoundException;
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

    public ParkingSpace findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Парковочное место с id " + id + " не найдено"));
    }

    public ParkingSpace save(ParkingSpace parkingSpace) {
        if (parkingSpace.id() != null && !repository.existById(parkingSpace.id())) {
            throw new ResourceNotFoundException("Парковочное место с id " + parkingSpace.id() + " не найдено");
        }
        return repository.save(parkingSpace);
    }

    public void delete(Integer id) {
        if (!repository.existById(id)) {
            throw new ResourceNotFoundException("Парковочное место с id " + id + " не найдено");
        }
        repository.delete(id);
    }
}

