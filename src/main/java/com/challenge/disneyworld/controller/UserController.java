package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.dto.ModelRegistrationUser;
import com.challenge.disneyworld.service.interfaces.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody ModelRegistrationUser user){
        ResponseEntity<?> response = userService.createUser(user);

        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
}
