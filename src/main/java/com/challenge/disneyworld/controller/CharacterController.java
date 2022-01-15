package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.service.CharacterService;
import com.challenge.disneyworld.utils.models.ModelCharacter;

import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Character;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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

    @GetMapping
    public ArrayList<ModelCharacter> getCharacter(){
        return characterService.getCharacters();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacter(@RequestBody Character character,
                                             @PathVariable(name = "id") Long id){

        return characterService.updateCharacter(character,id);
    }

    

    
}
