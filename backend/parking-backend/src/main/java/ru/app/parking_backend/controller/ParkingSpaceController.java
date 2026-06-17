package ru.app.parking_backend.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.dto.PageResponse;
import ru.app.parking_backend.entity.ParkingSpace;
import ru.app.parking_backend.service.ParkingSpaceService;

@RestController
@RequestMapping("/api/parking-spaces")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ParkingSpaceController {
    private final ParkingSpaceService service;

    @GetMapping
    public PageResponse<ParkingSpace> findAll(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return service.findAll(search, page, size);
    }

    @GetMapping("/{id}")
    public ParkingSpace findById(@PathVariable Integer id) {
        return service.findById(id);
    }

    @PostMapping
    public ParkingSpace save(@Valid @RequestBody ParkingSpace parkingSpace) {
        return service.save(parkingSpace);
    }

    @PutMapping("/{id}")
    public ParkingSpace update(@PathVariable Integer id, @Valid @RequestBody ParkingSpace parkingSpace) {
        ParkingSpace updatedSpace = new ParkingSpace(id, parkingSpace.numberSpace());
        return service.save(updatedSpace);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
