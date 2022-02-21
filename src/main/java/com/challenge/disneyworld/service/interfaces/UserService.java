package com.challenge.disneyworld.service.interfaces;

import com.challenge.disneyworld.dto.ModelRegistrationUser;

import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<?> createUser(ModelRegistrationUser user);
}
