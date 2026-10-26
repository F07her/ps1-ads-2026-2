import br.edu.fatecfranca.api.dtos.UserResponse;
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

import jakarta.validation.Valid;

import br.edu.fatecfranca.api.dtos.LoginRequest;
import br.edu.fatecfranca.api.dtos.UserResponse;


@RestController
@RequestMapping("/users")
public class UserController {

	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@PostMapping("/login")
   public ResponseEntity<UserResponse> login(
           @Valid @RequestBody LoginRequest request) {


       return service.authenticate(request)
               .map(UserResponse::new)
               .map(ResponseEntity::ok)
               .orElseGet(() -> ResponseEntity
                       .status(HttpStatus.UNAUTHORIZED)
                       .build());
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
   public ResponseEntity<UserResponse> create(
           @RequestBody UserRequest request) {


       User savedUser = service.create(request);


       return ResponseEntity
               .status(HttpStatus.CREATED)
               .body(new UserResponse(savedUser));
   }


	@GetMapping
   public List<UserResponse> findAll() {
       return service.findAll()
               .stream()
               .map(UserResponse::new)
               .toList();
   }


	@GetMapping("/{id}")
   public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
       return service.findById(id)
               .map(UserResponse::new)
               .map(ResponseEntity::ok)
               .orElse(ResponseEntity.notFound().build());
   }


	@PutMapping("/{id}")
   public ResponseEntity<UserResponse> update(
           @PathVariable Long id,
           @RequestBody UserRequest request) {


       if (!service.existsById(id)) {
           return ResponseEntity.notFound().build();
       }


       User updatedUser = service.update(id, request);


       return ResponseEntity.ok(new UserResponse(updatedUser));
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
