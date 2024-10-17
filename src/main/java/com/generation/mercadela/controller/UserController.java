package com.generation.mercadela.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.generation.mercadela.dto.UserLoginDTO;
import com.generation.mercadela.model.User;
import com.generation.mercadela.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserController {

    private final UserService userService;

    @GetMapping("/all")
    public ResponseEntity<List<UserLoginDTO>> findAll() {
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserLoginDTO> getById(@PathVariable Long id) {
        return userService.getById(id);
    }

    @PostMapping("/login")
    public ResponseEntity<UserLoginDTO> login(@RequestBody @Valid UserLoginDTO UserLoginDTO) {
        return userService.login(UserLoginDTO);
    }

    @PostMapping("/register")
    public ResponseEntity<UserLoginDTO> register(@RequestBody @Valid User user) {
        return userService.register(user);
    }

    @PutMapping("/update")
    public ResponseEntity<UserLoginDTO> update(@Valid @RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return userService.delete(id);
    }
}
