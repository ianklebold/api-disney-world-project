package com.challenge.disneyworld.service.interfaces;

import com.challenge.disneyworld.dto.ModelRegistrationUser;


import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<?> createUser(ModelRegistrationUser user);
    public ResponseEntity<?> createAdmin(String email);
    public ResponseEntity<?> deleteUser(Long id);
}
