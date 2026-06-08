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
    private final ParkingSpaceService service;

    @GetMapping
    public List<ParkingSpace> getAll() {
        return service.findAll();
    }

    // эта функция делает запрос на получение места по его id
    @GetMapping("/{id}")
    public ParkingSpace getById(@PathVariable Integer id) {
        return service.getSpaceById(id).orElse(null);
    }

    // эта функция сохраняет новое место в базу
    @PostMapping
    public ParkingSpace create(@RequestBody ParkingSpace parkingSpace) {
        return service.saveSpace(parkingSpace);
    }

    // эта функция обновляет парковочное место
    @PutMapping("/{id}")
    public ParkingSpace update(@PathVariable Integer id, @RequestBody ParkingSpace parkingSpace) {
        ParkingSpace updatedSpace = new ParkingSpace(id, parkingSpace.numberSpace());
        return service.saveSpace(updatedSpace);
    }

    // эта функция удаляет место из базы
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
