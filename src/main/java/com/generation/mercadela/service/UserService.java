package com.generation.mercadela.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.mercadela.model.User;
import com.generation.mercadela.model.UserLogin;
import com.generation.mercadela.repository.UserRepository;
import com.generation.mercadela.security.JwtService;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public Optional<User> register(User user) {
		if (userRepository.findByEmail(user.getEmail()).isPresent()) {
			return Optional.empty();
		}
		user.setPassword(criptografarSenha(user.getPassword()));
		return Optional.of(userRepository.save(user));
	}

	public Optional<User> atualizaruser(User user) {
		if (userRepository.findById(user.getId()).isPresent()) {
			Optional<User> buscauser = userRepository.findByEmail(user.getEmail());
			if (buscauser.isPresent() && !Objects.equals(buscauser.get().getId(), user.getId())) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			}

			// Verifica se a senha foi alterada antes de criptografá-la
			if (!user.getPassword().isEmpty()) {
				user.setPassword(criptografarSenha(user.getPassword()));
			}

			return Optional.ofNullable(userRepository.save(user));
		}
		return Optional.empty();
	}

	public Optional<UserLogin> autenticaruser(Optional<UserLogin> userLogin) {
		try {
			var credenciais = new UsernamePasswordAuthenticationToken(userLogin.get().getEmail(),
					userLogin.get().getPassword());
			Authentication authentication = authenticationManager.authenticate(credenciais);

			if (authentication.isAuthenticated()) {
				return buscarDadosUsuario(userLogin);
			}
		} catch (AuthenticationException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
		}
		return Optional.empty();
	}

	private Optional<UserLogin> buscarDadosUsuario(Optional<UserLogin> userLogin) {
		Optional<User> user = userRepository.findByEmail(userLogin.get().getEmail());

		if (user.isPresent()) {
			// Preenche o Objeto userLogin com os dados encontrados
			userLogin.get().setId(user.get().getId());
			userLogin.get().setCpf(user.get().getCpf());
			userLogin.get().setName(user.get().getName());
			userLogin.get().setPhoto(user.get().getPhoto());
			userLogin.get().setGender(user.get().getGender());
			userLogin.get().setToken(gerarToken(userLogin.get().getEmail()));
			userLogin.get().setPassword(""); // Remover a senha da resposta

			return userLogin;
		}
		return Optional.empty();
	}

	private String criptografarSenha(String senha) {
		return new BCryptPasswordEncoder().encode(senha);
	}

	private String gerarToken(String user) {
		return "Bearer " + jwtService.generateToken(user);
	}
}
