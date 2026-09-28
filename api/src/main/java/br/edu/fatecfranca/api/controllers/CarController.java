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
import br.edu.fatecfranca.api.services.CarService;

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
    public ResponseEntity<Car> create(@RequestBody Car car) {
        if (car == null) {
            return ResponseEntity.badRequest().build();
        }

        if (car.getModel() == null || car.getModel().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (car.getBrand() == null || car.getBrand().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (car.getColor() == null || car.getColor().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (car.getManufactureYear() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (car.getLicensePlate() == null || car.getLicensePlate().isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        if (car.getCustomer() == null || car.getCustomer().getId() == null) {
            return ResponseEntity.badRequest().build();
        }

        if (car.getId() != null && service.existsById(car.getId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        if (car.getImported() == null) {
            car.setImported(false);
        }

        Car savedCar = service.create(car);
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
    public ResponseEntity<Car> update(@PathVariable Long id, @RequestBody Car car) {
        if (car == null) {
            return ResponseEntity.badRequest().build();
        }

        if (!service.existsById(id)) {
            return ResponseEntity.notFound().build();
        }

        car.setId(id);
        Car updatedCar = service.update(car);
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
}

// comentario para o git funcionar
// a coisa funciona, mas o git não quer aceitar o commit sem um comentário,
//  então aqui vai um comentário para o git aceitar o commit.
// porque eu esqueci de colocar o PRINTS_PROVA1 corretamente.