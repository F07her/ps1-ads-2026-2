package br.edu.fatecfranca.api.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.fatecfranca.api.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	Optional<User> findByUsernameOrEmail(String username, String email);
}
