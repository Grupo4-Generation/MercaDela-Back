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

import com.generation.mercadela.model.Usuario;
import com.generation.mercadela.model.UsuarioLogin;
import com.generation.mercadela.repository.UsuarioRepository;
import com.generation.mercadela.security.JwtService;

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
		if (usuarioRepository.findById(usuario.getId()).isPresent()) {
			Optional<Usuario> buscaUsuario = usuarioRepository.findByEmailUsuario(usuario.getEmailUsuario());
			if ((buscaUsuario.isPresent()) && (buscaUsuario.get().getId() != usuario.getId()))
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);

			usuario.setSenhaUsuario(criptografarSenha(usuario.getSenhaUsuario()));
			
			return Optional.ofNullable(usuarioRepository.save(usuario));
		}
		return Optional.empty();
	}
	
	public Optional<UsuarioLogin> autenticarUsuario(Optional<UsuarioLogin> usuarioLogin){
		var credenciais = new UsernamePasswordAuthenticationToken(usuarioLogin.get().getUsuario(),
				usuarioLogin.get().getSenha());
		
		Authentication authentication = authenticationManager.authenticate(credenciais);
		
		if (authentication.isAuthenticated()) {
			Optional<Usuario> usuario = usuarioRepository.findByEmailUsuario(usuarioLogin.get().getUsuario());
			
			if(usuario.isPresent()) {
				usuarioLogin.get().setId(usuario.get().getId());
				usuarioLogin.get().setNome(usuario.get().getNomeUsuario());
				usuarioLogin.get().setToken(gerarToken(usuarioLogin.get().getUsuario()));
				usuarioLogin.get().setSenha("");

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