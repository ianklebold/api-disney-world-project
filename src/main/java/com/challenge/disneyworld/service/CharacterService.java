package com.challenge.disneyworld.service;

import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.entity.Character;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    @Autowired
    CharacterRepository characterRepository;


    public Character createCharacter(Character character){
        return characterRepository.save(character);
    }
}
