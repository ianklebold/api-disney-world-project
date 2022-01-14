package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.service.CharacterService;
import com.challenge.disneyworld.entity.Character;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disneyworld/api/v1/characters")
public class CharacterController {
    @Autowired
    CharacterService characterService;

    @PostMapping
    public ResponseEntity<?> createCharacter(@RequestBody Character character){
        
        return new ResponseEntity<>(characterService.createCharacter(character),
                                    HttpStatus.CREATED);
    } 
}
