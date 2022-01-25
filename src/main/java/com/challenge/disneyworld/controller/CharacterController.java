package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.service.CharacterService;

import java.util.ArrayList;

import com.challenge.disneyworld.entity.Character;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping()
    public ResponseEntity<?> createCharacter(@RequestBody Character character){
        
        return characterService.createCharacter(character);
    } 

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacterById(@PathVariable(name = "id") Long id){
        return characterService.getCharacterById(id);
    }

    @GetMapping
    public ResponseEntity<?> getCharacter(){
        return characterService.getCharacter();
    }

    @GetMapping("/all")
    public ArrayList<Character> getallCharacter(){
        return characterService.getAll();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacter(@RequestBody Character character,
                                             @PathVariable(name = "id") Long id){

        return characterService.updateCharacter(character,id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable(name = "id") Long id){
        return characterService.deleteCharacter(id);
    }

    

    
}
