package ru.app.parking_backend.controller;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.entity.ParkingSpace;
import ru.app.parking_backend.service.ParkingSpaceService;

import java.util.List;

@RestController
@RequestMapping("/api/parking-spaces")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ParkingSpaceController {
    private final ParkingSpaceService service;

    @GetMapping
    public List<ParkingSpace> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ParkingSpace findById(@PathVariable Integer id) {
        return service.findById(id).orElse(null);
    }

    @PostMapping
    public ParkingSpace save(@Valid @RequestBody ParkingSpace parkingSpace) {
        return service.save(parkingSpace);
    }

    @PutMapping("/{id}")
    public ParkingSpace update(@PathVariable Integer id, @Valid  @RequestBody ParkingSpace parkingSpace) {
        ParkingSpace updatedSpace = new ParkingSpace(id, parkingSpace.numberSpace());
        return service.save(updatedSpace);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
