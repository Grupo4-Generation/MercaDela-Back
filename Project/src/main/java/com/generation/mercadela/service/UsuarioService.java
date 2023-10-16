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

import com.generation.mercadela.model.UsuarioLogin;
import com.generation.mercadela.model.Usuario;
import com.generation.mercadela.repository.UsuarioRepository;
import com.generation.mercadela.security.JwtService;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private JwtService jwtService;

	@Autowired
	private AuthenticationManager authenticationManager;

	public Optional<Usuario> cadastrarUsuario(Usuario usuario) {

		if (usuarioRepository.findByEmailUsuario(usuario.getEmailUsuario()).isPresent())
			return Optional.empty();

		usuario.setSenhaUsuario(criptografarSenha(usuario.getSenhaUsuario()));

		return Optional.of(usuarioRepository.save(usuario));

	}

	public Optional<Usuario> atualizarUsuario(Usuario usuario) {

		if(usuarioRepository.findById(usuario.getId()).isPresent()) {

			Optional<Usuario> buscaUsuario = usuarioRepository.findByEmailUsuario(usuario.getEmailUsuario());

			if ( (buscaUsuario.isPresent()) && ( buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			usuario.setSenhaUsuario(criptografarSenha(usuario.getSenhaUsuario()));

			return Optional.ofNullable(usuarioRepository.save(usuario));

		}

		return Optional.empty();

	}

	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin) {

		// Gera o Objeto de autenticação
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getEmailusuario(), usuarioLogin.get().getSenhaUsuario());

		// Autentica o Usuario
		Authentication authentication = authenticationManager.authenticate(credenciais);

		// Se a autenticação foi efetuada com sucesso
		if (authentication.isAuthenticated()) {

			// Busca os dados do usuário
			Optional<Usuario> usuario = usuarioRepository.findByEmailUsuario(usuarioLogin.get().getEmailusuario());

			// Se o usuário foi encontrado
			if (usuario.isPresent()) {

				// Preenche o Objeto usuarioLogin com os dados encontrados
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNomeUsuario(usuario.get().getNomeUsuario());
				usuarioLogin.get().setFoto(usuario.get().getFoto());
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getToken()));
				usuarioLogin.get().setSenhaUsuario("");
				usuarioLogin.get().setGeneroUsuario(usuario.get().getGeneroUsuario());

				// Retorna o Objeto preenchido
				return usuarioLogin;

			}

		}

		return Optional.empty();

	}

	private String criptografarSenha(String senha) {

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		return encoder.encode(senha);

	}

	private String gerarToken(String usuario) {
		return "Bearer " + jwtService.generateToken(usuario);
	}

}