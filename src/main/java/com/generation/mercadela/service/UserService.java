package com.generation.mercadela.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.mercadela.model.UserLogin;
import com.generation.mercadela.model.User;
import com.generation.mercadela.repository.UserRepository;
import com.generation.mercadela.security.JwtService;

@Service
public class UserService{

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public Optional<User> register(User user) {

		if (userRepository.findByEmail(user.getEmail()).isPresent())
			return Optional.empty();

		user.setPassword(criptografarSenha(user.getPassword()));

		return Optional.of(userRepository.save(user));

	}

	public Optional<User> atualizaruser(User user) {

		if(userRepository.findById(user.getId()).isPresent()) {

			Optional<User> buscauser = userRepository.findByEmail(user.getEmail());

			if ( (buscauser.isPresent()) && ( buscauser.get().getId() != user.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			user.setPassword(criptografarSenha(user.getPassword()));

			return Optional.ofNullable(userRepository.save(user));

		}

		return Optional.empty();

	}

	public Optional<UserLogin> autenticaruser(Optional<UserLogin> userLogin) {

		// Gera o Objeto de autenticação
		var credenciais = new UsernamePasswordAuthenticationToken(userLogin.get().getEmail(), userLogin.get().getPassword());

		// Autentica o user
		Authentication authentication = authenticationManager.authenticate(credenciais);

		// Se a autenticação foi efetuada com sucesso
		if (authentication.isAuthenticated()) {

			// Busca os dados do usuário
			Optional<User> user = userRepository.findByEmail(userLogin.get().getEmail());

			// Se o usuário foi encontrado
			if (user.isPresent()) {

				// Preenche o Objeto userLogin com os dados encontrados
				userLogin.get().setId(user.get().getId());
				userLogin.get().setCpf(user.get().getCpf());
				userLogin.get().setName(user.get().getName());
				userLogin.get().setPhoto(user.get().getPhoto());
				userLogin.get().setGender(user.get().getGender());
				userLogin.get().setToken(gerarToken(userLogin.get().getEmail()));
				userLogin.get().setPassword("");

				// Retorna o Objeto preenchido
				return userLogin;

			}

		}

		return Optional.empty();

	}

	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);

	}

	private String gerarToken(String user) {
		return "Bearer " + jwtService.generateToken(user);
	}

}