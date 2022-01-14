package com.challenge.disneyworld.service;

import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.utils.models.ModelCharacter;
import com.challenge.disneyworld.utils.models.builders.BuilderCharacter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;

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

    public ArrayList<ModelCharacter> getCharacters(){
        ArrayList<Character> characters = (ArrayList<Character>) characterRepository.findAll();
 
        if(characters.size() > 0){
             BuilderCharacter builder = new BuilderCharacter();
 
             ArrayList<ModelCharacter> charactersRequest = new ArrayList<ModelCharacter>();
 
                 for (Character c : characters) {
                     charactersRequest.add(
                         builder.setId(c.getId())
                             .setAge(returnAge(c))
                             .setName(c.getName())
                             .setHistory(c.getHistory())
                             .setAppearances(c.getAppearances())
                             .builder()
                     );
                 }
 
                 return charactersRequest;
         }else{
                 return null;
         }
 
     }
 
     public int returnAge(Character character){
         

        /*LocalDate localDate1 = LocalDate.parse(character.getBorn_date().toString(),
            DateTimeFormatter.ofPattern("yyyy-MM-dd"));    */
        
        Period age = Period.between(character.getBorn_date(), LocalDate.now());
        return age.getYears();
     }
}
