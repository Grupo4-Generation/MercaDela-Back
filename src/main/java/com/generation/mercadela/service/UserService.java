package com.generation.mercadela.service;

import java.util.Objects;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.generation.mercadela.model.User;
import com.generation.mercadela.model.UserLogin;
import com.generation.mercadela.repository.UserRepository;
import com.generation.mercadela.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    
    public Optional<User> register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return Optional.empty();
        }
        user.setPassword(encryptPassword(user.getPassword()));
        return Optional.of(userRepository.save(user));
    }

    public Optional<User> update(User user) {
        if (userRepository.findById(user.getId()).isPresent()) {
            Optional<User> buscauser = userRepository.findByEmail(user.getEmail());
            if (buscauser.isPresent() && !Objects.equals(buscauser.get().getId(), user.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
            }

            // Verifica se a senha foi alterada antes de criptografá-la
            if (!user.getPassword().isEmpty()) {
                user.setPassword(encryptPassword(user.getPassword()));
            }

            return Optional.ofNullable(userRepository.save(user));
        }
        return Optional.empty();
    }

    public Optional<UserLogin> login(Optional<UserLogin> userLogin) {
        try {
            var credenciais = new UsernamePasswordAuthenticationToken(userLogin.get().getEmail(),
                    userLogin.get().getPassword());
            Authentication authentication = authenticationManager.authenticate(credenciais);

            if (authentication.isAuthenticated()) {
                return findData(userLogin);
            }
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas");
        }
        return Optional.empty();
    }

    private Optional<UserLogin> findData(Optional<UserLogin> userLogin) {
        Optional<User> user = userRepository.findByEmail(userLogin.get().getEmail());

        if (user.isPresent()) {
            // Preenche o Objeto userLogin com os dados encontrados
            userLogin.get().setId(user.get().getId());
            userLogin.get().setCpf(user.get().getCpf());
            userLogin.get().setName(user.get().getName());
            userLogin.get().setPhoto(user.get().getPhoto());
            userLogin.get().setGender(user.get().getGender());
            userLogin.get().setToken(createToken(userLogin.get().getEmail()));
            userLogin.get().setPassword("");

            return userLogin;
        }
        return Optional.empty();
    }

    public User getLoggedInUser(){
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> loggedInUser = userRepository.findByEmail(userDetails.getUsername());
        return loggedInUser.get();
    }

    private String encryptPassword(String senha) {
        return new BCryptPasswordEncoder().encode(senha);
    }

    private String createToken(String user) {
        return "Bearer " + jwtService.generateToken(user);
    }
}
