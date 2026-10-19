package br.edu.fatecfranca.api.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.entities.Car;
import br.edu.fatecfranca.api.dtos.CarRequest;
import br.edu.fatecfranca.api.services.CarService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final CarService service;

    public CarController(CarService service) {
        this.service = service;
    }

    @GetMapping("/status")
    public ResponseEntity<String> status() {
        try {
            long count = service.count();
            return ResponseEntity.ok("Tabela 'cars' existe com " + count + " registros");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao acessar tabela: " + e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<Car> create( @Valid @RequestBody CarRequest request) {
        if (isInvalid(request)) {
            return ResponseEntity.badRequest().build();
        }

        if (!service.customerExistsById(request.customerId())) {
            return ResponseEntity.badRequest().build();
        }

        Car savedCar = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCar);
    }

    @GetMapping
    public List<Car> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> findById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> update(@PathVariable Long id, @Valid @RequestBody CarRequest request) {
        if (isInvalid(request)) {
            return ResponseEntity.badRequest().build();
        }

        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        if (!service.customerExistsById(request.customerId())) {
            return ResponseEntity.badRequest().build();
        }

        Car updatedCar = service.update(id, request);
        return ResponseEntity.ok(updatedCar);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        service.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private boolean isInvalid(CarRequest request) {
        return request == null
                || request.model() == null || request.model().isBlank()
                || request.brand() == null || request.brand().isBlank()
                || request.color() == null || request.color().isBlank()
                || request.manufactureYear() == null
                || request.licensePlate() == null || request.licensePlate().isBlank()
                || request.customerId() == null;
    }
}

// comentario para o git funcionar
// a coisa funciona, mas o git não quer aceitar o commit sem um comentário,
//  então aqui vai um comentário para o git aceitar o commit.
// porque eu esqueci de colocar o PRINTS_PROVA1 corretamente.