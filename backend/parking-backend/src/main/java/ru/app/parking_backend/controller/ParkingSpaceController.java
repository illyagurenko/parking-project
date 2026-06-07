package ru.app.parking_backend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.dto.UpdateSpaceRequest;
import ru.app.parking_backend.entity.ParkingSpace;
import ru.app.parking_backend.repository.ParkingSpaceRepository;
import ru.app.parking_backend.service.ParkingSpaceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parking")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ParkingSpaceController {
    private final ParkingSpaceService parkingSpaceService;

    @GetMapping
    public List<ParkingSpace> findAllParkingSpace(){
        return parkingSpaceService.findAllParkingSpace();
    }

    @GetMapping("/{number}")
    public Optional<ParkingSpace> findParkingSpaceByNumber(@PathVariable("number") String number){
        return parkingSpaceService.findParkingSpaceByNumber(number);
    }

    @PostMapping
    public ParkingSpace saveParkingSpace(@RequestBody ParkingSpace space){
        return parkingSpaceService.saveParkingSpace(space);
    }

    @PutMapping("/{id}")
    public void updateParkingSpaceNumber(@PathVariable Integer id, @RequestBody UpdateSpaceRequest request){
        parkingSpaceService.updateParkingSpaceNumber(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteParkingSpace(Integer id){
        parkingSpaceService.deleteParkingSpace(id);
    }
}
