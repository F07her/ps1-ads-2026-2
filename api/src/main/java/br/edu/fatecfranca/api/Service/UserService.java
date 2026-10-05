package br.edu.fatecfranca.api.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import br.edu.fatecfranca.api.entities.User;
import br.edu.fatecfranca.api.dtos.UserRequest;
import br.edu.fatecfranca.api.repositories.UserRepository;

@Service
public class UserService {

	private final UserRepository repository;

	private void copyToEntity(UserRequest request, User user) {
		user.setFullname(request.fullname());
		user.setUsername(request.username());
		user.setEmail(request.email());
		user.setPassword(request.password());
		user.setIsAdmin(request.isAdmin() != null ? request.isAdmin() : false);
	}

	public UserService(UserRepository repository) {
		this.repository = repository;
	}

	public long count() {
		return repository.count();
	}

	public User create(User user) {
		return repository.save(user);
	}

	public User create(UserRequest request) {
		User user = new User();
		copyToEntity(request, user);
		return repository.save(user);
	}

	public boolean existsByUsername(String username) {
		return repository.existsByUsername(username);
	}

	public boolean existsByEmail(String email) {
		return repository.existsByEmail(email);
	}

	public List<User> findAll() {
		return repository.findAll();
	}

	public Optional<User> findById(Long id) {
		return repository.findById(id);
	}

	public User update(User user) {
		return repository.save(user);
	}

	public User update(Long id, UserRequest request) {
		Optional<User> optionalUser = repository.findById(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			copyToEntity(request, user);
			return repository.save(user);
		} else {
			throw new RuntimeException("User not found with id: " + id);
		}
	}

	public boolean existsById(Long id) {
		return repository.existsById(id);
	}

	public void deleteById(Long id) {
		repository.deleteById(id);
	}
}
