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
    private final ClientService clientService;

    @GetMapping
    public List<Client> list(@RequestParam(required = false) String name) {
        return clientService.findAll(name);
    }

    @PostMapping
    public void create(@RequestBody Client client) {
        clientService.create(client);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody Client client) {
        clientService.update(id, client);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        clientService.delete(id);
    }
}
