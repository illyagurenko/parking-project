package ru.app.parking_backend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.entity.Car;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.service.ClientService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClientController {
    private final ClientService clientService;

    @GetMapping
    public List<Client> findAllClient(){
        return clientService.findAllClient();
    }

    @GetMapping("/{full_name}")
    public Optional<Client> findClientByFullName(@PathVariable String fullName){
        return clientService.findClientByFullName(fullName);
    }

    @PostMapping
    public Client saveClient(@RequestBody Client client){
        return clientService.saveClient(client);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(Integer id){
        clientService.deleteClient(id);
    }
}
