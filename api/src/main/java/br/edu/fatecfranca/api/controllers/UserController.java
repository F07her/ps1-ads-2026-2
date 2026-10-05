package br.edu.fatecfranca.api.controllers;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.edu.fatecfranca.api.entities.User;
import br.edu.fatecfranca.api.dtos.UserRequest;
import br.edu.fatecfranca.api.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping("/status")
	public ResponseEntity<String> status() {
		try {
			long count = service.count();
			return ResponseEntity.ok("Tabela 'users' existe com " + count + " registros");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Erro ao acessar tabela: " + e.getMessage());
		}
	}

	@PostMapping
	public ResponseEntity<User> create(@RequestBody UserRequest request) {
		if (isInvalid(request)) {
			return ResponseEntity.badRequest().build();
		}

		if (service.existsByUsername(request.username()) || service.existsByEmail(request.email())) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}

		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(service.create(request));
		} catch (DataIntegrityViolationException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).build();
		}
	}

	@GetMapping
	public List<User> findAll() {
		return service.findAll();
	}

	@GetMapping("/{id}")
	public ResponseEntity<User> findById(@PathVariable Long id) {
		return service.findById(id)
				.map(ResponseEntity::ok)
				.orElse(ResponseEntity.notFound().build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<User> update(@PathVariable Long id, @RequestBody UserRequest request) {
		if (isInvalid(request)) {
			return ResponseEntity.badRequest().build();
		}

		return service.findById(id)
				.map(user -> {
					if ((!user.getUsername().equals(request.username())
							&& service.existsByUsername(request.username()))
							|| (!user.getEmail().equals(request.email())
									&& service.existsByEmail(request.email()))) {
						return ResponseEntity.status(HttpStatus.CONFLICT).<User>build();
					}

					try {
						return ResponseEntity.ok(service.update(id, request));
					} catch (DataIntegrityViolationException e) {
						return ResponseEntity.status(HttpStatus.CONFLICT).<User>build();
					}
				})
				.orElse(ResponseEntity.notFound().build());
	}

	@PatchMapping("/{id}/email")
	public ResponseEntity<User> updateEmail(@PathVariable Long id, @RequestBody EmailUpdateRequest request) {
		if (request == null || request.email() == null || request.email().isBlank()) {
			return ResponseEntity.badRequest().build();
		}

		return service.findById(id)
				.map(user -> {
					if (!user.getEmail().equals(request.email()) && service.existsByEmail(request.email())) {
						return ResponseEntity.status(HttpStatus.CONFLICT).<User>build();
					}

					user.setEmail(request.email());
					try {
						return ResponseEntity.ok(service.update(user));
					} catch (DataIntegrityViolationException e) {
						return ResponseEntity.status(HttpStatus.CONFLICT).<User>build();
					}
				})
				.orElse(ResponseEntity.notFound().build());
	}

	public record EmailUpdateRequest(String email) {
	}

	private boolean isInvalid(UserRequest request) {
		return request == null
				|| request.fullname() == null || request.fullname().isBlank()
				|| request.username() == null || request.username().isBlank()
				|| request.email() == null || request.email().isBlank()
				|| request.password() == null || request.password().isBlank();
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
