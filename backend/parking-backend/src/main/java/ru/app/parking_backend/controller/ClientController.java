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
    public List<Client> getAll(@RequestParam(required = false) String search) {
        return service.getAllClients(search);
    }

    @GetMapping("/{id}")
    public Client getById(@PathVariable Integer id) {
        return service.getClientById(id).orElse(null);
    }

    @PostMapping
    public Client create(@RequestBody Client client) {
        return service.saveClient(client);
    }

    @PutMapping("/{id}")
    public Client update(@PathVariable Integer id, @RequestBody Client client) {
        Client updatedClient = new Client(id, client.fullName());
        return service.saveClient(updatedClient);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id);
    }
}
