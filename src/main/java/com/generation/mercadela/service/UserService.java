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
import com.generation.mercadela.model.User;
import com.generation.mercadela.repository.UserRepository;
import com.generation.mercadela.security.JwtService;
import com.generation.mercadela.security.UserDetailsImpl;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<List<UserLoginDTO>> findAll() {
        List<User> users = userRepository.findAll();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users.stream().map(this::convertToUserLoginDTO).toList());
    }

    public ResponseEntity<UserLoginDTO> getById(Long id) {
        return userRepository.findById(id)
                .map(user -> ResponseEntity.ok(convertToUserLoginDTO(user)))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    public ResponseEntity<UserLoginDTO> login(UserLoginDTO userLoginDTO) {
        try {
            var credentials = new UsernamePasswordAuthenticationToken(userLoginDTO.getEmail(), userLoginDTO.getPassword());
            Authentication authentication = authenticationManager.authenticate(credentials);

            if (authentication.isAuthenticated()) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                User user = userRepository.findByEmail(userLoginDTO.getEmail())
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

                String token = jwtService.generateToken(user.getEmail());

                userLoginDTO.setId(user.getId());
                userLoginDTO.setName(user.getName());
                userLoginDTO.setCpf(user.getCpf());
                userLoginDTO.setGender(user.getGender());
                userLoginDTO.setPhoto(user.getPhoto());
                userLoginDTO.setAdmin(user.isAdmin());
                userLoginDTO.setToken(token);
                System.out.println(userLoginDTO);
                return ResponseEntity.ok(userLoginDTO);
            }
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    public ResponseEntity<UserLoginDTO> register(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
        }
        user.setPassword(encryptPassword(user.getPassword()));
        User savedUser = userRepository.save(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(convertToUserLoginDTO(savedUser));
    }

    public ResponseEntity<UserLoginDTO> update(User user) {
        UserDetailsImpl currentUser = getCurrentAuthenticatedUser();

        if (!currentUser.getId().equals(user.getId()) && !currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to update this user");
        }

        userRepository.findByEmail(user.getEmail())
                .filter(foundUser -> !foundUser.getId().equals(user.getId()))
                .ifPresent(foundUser -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already in use");
                });

        if (!user.getPassword().isEmpty()) {
            user.setPassword(encryptPassword(user.getPassword()));
        }

        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(convertToUserLoginDTO(updatedUser));
    }

    @Transactional
    public ResponseEntity<Void> delete(Long id) {
        UserDetailsImpl currentUser = getCurrentAuthenticatedUser();

        if (!currentUser.getId().equals(id) && !currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to delete this user");
        }

        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }

        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private String encryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    private UserLoginDTO convertToUserLoginDTO(User user) {
        UserLoginDTO userLoginDTO = new UserLoginDTO();
        userLoginDTO.setId(user.getId());
        userLoginDTO.setEmail(user.getEmail());
        userLoginDTO.setName(user.getName());
        userLoginDTO.setPhoto(user.getPhoto());
        userLoginDTO.setAdmin(user.isAdmin());
        userLoginDTO.setCpf(user.getCpf());
        userLoginDTO.setGender(user.getGender());
        userLoginDTO.setProduct(user.getProducts());
        return userLoginDTO;
    }

    private UserDetailsImpl getCurrentAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl) {
            return (UserDetailsImpl) authentication.getPrincipal();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not authenticated");
    }
}
