package ru.app.parking_backend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.entity.ParkingSpace;
import ru.app.parking_backend.service.ParkingSpaceService;

import java.util.List;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ParkingSpaceController {
    private final ParkingSpaceService parkingSpaceService;

    @GetMapping
    public List<ParkingSpace> list() {
        return parkingSpaceService.findAll();
    }

    @GetMapping("/available")
    public List<ParkingSpace> listAvailable() {
        return parkingSpaceService.listAvailable();
    }

    @PostMapping
    public void create(@RequestBody ParkingSpace space) {
        parkingSpaceService.create(space);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody ParkingSpace space) {
        parkingSpaceService.update(id, space);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        parkingSpaceService.delete(id);
    }
}
