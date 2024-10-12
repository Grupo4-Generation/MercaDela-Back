package com.generation.mercadela.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.generation.mercadela.dto.UserLoginDTO;
import com.generation.mercadela.dto.UserResponseDTO;
import com.generation.mercadela.model.User;
import com.generation.mercadela.repository.UserRepository;
import com.generation.mercadela.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<List<UserResponseDTO>> findAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users.stream().map(this::convertToUserResponseDTO).toList());
    }

    public ResponseEntity<UserResponseDTO> getById(Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(convertToUserResponseDTO(user)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); // 404
    }

    public ResponseEntity<UserLoginDTO> login(UserLoginDTO userLogin) {
        try {
            var credentials = new UsernamePasswordAuthenticationToken(userLogin.getEmail(), userLogin.getPassword());
            Authentication authentication = authenticationManager.authenticate(credentials);

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                UserLoginDTO loggedInUser = prepareUserLoginData(userLogin);
                return ResponseEntity.ok(loggedInUser); // 200 OK
            }
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials"); // 401 Unauthorized
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<UserResponseDTO> register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered"); // 400 Bad Request
        }
        user.setPassword(encryptPassword(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToUserResponseDTO(savedUser)); // 201 Created
    }

    public ResponseEntity<UserResponseDTO> update(User user) {
        userRepository.findByEmail(user.getEmail())
                .filter(foundUser -> !foundUser.getId().equals(user.getId()))
                .ifPresent(foundUser -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use"); // 400
                });

        if (!user.getPassword().isEmpty()) {
            user.setPassword(encryptPassword(user.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(convertToUserResponseDTO(updatedUser)); // 200 OK
    }

    @Transactional
    public ResponseEntity<Void> delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"); // 404
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    private UserLoginDTO prepareUserLoginData(UserLoginDTO userLogin) {
        User user = userRepository.findByEmail(userLogin.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")); // 404

        userLogin.setId(user.getId());
        userLogin.setEmail(user.getEmail());
        userLogin.setToken(createToken(user.getEmail()));
        userLogin.setAdmin(user.isAdmin());

        return userLogin;
    }

    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    private String createToken(String email) {
        return "Bearer " + jwtService.generateToken(email);
    }

    private UserResponseDTO convertToUserResponseDTO(User user) {
        UserResponseDTO responseDTO = new UserResponseDTO();
        responseDTO.setId(user.getId());
        responseDTO.setEmail(user.getEmail());
        responseDTO.setName(user.getName());
        responseDTO.setPhoto(user.getPhoto());
        responseDTO.setAdmin(user.isAdmin());
        responseDTO.setProduct(user.getProducts());
        return responseDTO;
    }
}
