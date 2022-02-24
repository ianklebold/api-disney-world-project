package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.service.interfaces.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable(name = "id") Long id){
        ResponseEntity<?> response = userService.deleteUser(id);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @GetMapping
    public ResponseEntity<?> getUser(){
        ResponseEntity<?> response = userService.getUser();
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }
}
