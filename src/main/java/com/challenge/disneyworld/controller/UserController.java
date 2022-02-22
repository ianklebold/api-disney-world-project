package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.dto.ModelRegistrationUser;
import com.challenge.disneyworld.service.MailService;
import com.challenge.disneyworld.service.interfaces.UserService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {
    private final UserService userService;
    private final MailService mailService;


    @PostMapping("/register")
    public ResponseEntity<?> createUser(@RequestBody ModelRegistrationUser user){
        ResponseEntity<?> response = userService.createUser(user);
        if(response.getStatusCodeValue() == 200){
            //Enviamos email de bienvenida
            try {
                mailService.send(user);    
            } catch (Exception e) {
                System.out.println("Error al enviar el email");
            }
        }
        return new ResponseEntity<>(response.getBody(), response.getStatusCode());
    }
}
