package ru.app.parking_backend.controller;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.app.parking_backend.entity.Client;
import ru.app.parking_backend.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class ClientController {
    private final ClientService service;

    @GetMapping
    public List<Client> findAll(@RequestParam(required = false) String search) {
        return service.findAll(search);
    }

    @GetMapping("/{id}")
    public Client findById(@PathVariable Integer id) {
        return service.findById(id).orElse(null);
    }

    @PostMapping
    public Client save(@RequestBody Client client) {
        return service.save(client);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Integer id, @RequestBody Client client) {
        Client updatedClient = new Client(id, client.fullName());
        return service.save(updatedClient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
