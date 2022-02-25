package com.challenge.disneyworld.service;

import com.challenge.disneyworld.entity.Character;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.service.interfaces.CharacterService;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class CharacterServiceTest {
    @Mock
    private CharacterRepository characterRepository;

    @InjectMocks
    private CharacterService characterService;

    private Character characterValid;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
        characterValid = new Character();
        characterValid.setName("Mickey");
        characterValid.setBorn_date(LocalDate.of(2022,8,10));
        characterValid.setHistory("This is my history");
        characterValid.setWeight(50.0);
        characterValid.setId((long) 1);

    }

    @Test
    public void return_code_status_200_when_the_character_is_created_with_succesfully(Character character){
        verify(characterService, times(1)).createCharacter(null, null, character);
        
    }
}
