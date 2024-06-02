package com.generation.mercadela.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.generation.mercadela.model.UserLogin;
import com.generation.mercadela.model.User;
import com.generation.mercadela.repository.UserRepository;
import com.generation.mercadela.service.UserService;

import jakarta.validation.Valid;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/all")
	public ResponseEntity <List<User>> getAll(){

		return ResponseEntity.ok(userRepository.findAll());

	}

	@GetMapping("/{id}")
	public ResponseEntity<User> getById(@PathVariable Long id) {
		return userRepository.findById(id)
				.map(resposta -> ResponseEntity.ok(resposta))
				.orElse(ResponseEntity.notFound().build());
	}

	@PostMapping("/logar")
	public ResponseEntity<UserLogin> autenticaruser(@RequestBody Optional<UserLogin> userLogin){

		return userService.autenticaruser(userLogin)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}


	@PostMapping("/cadastrar")
	public ResponseEntity<User> postuser(@RequestBody @Valid User user) {

		return userService.cadastraruser(user)
				.map(resposta -> ResponseEntity.status(HttpStatus.CREATED).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

	}

	@PutMapping("/atualizar")
	public ResponseEntity<User> putuser(@Valid @RequestBody User user) {

		return userService.atualizaruser(user)
				.map(resposta -> ResponseEntity.status(HttpStatus.OK).body(resposta))
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());

	}

	@ResponseStatus(HttpStatus.NO_CONTENT)
	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		Optional<User> user = userRepository.findById(id);

		if(user.isEmpty())
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		userRepository.deleteById(id);
	}
}