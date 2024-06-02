package com.generation.mercadela.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import com.generation.mercadela.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	public Optional<User> findByEmail(@Param("email") String email);

}