package com.challenge.disneyworld.service;

import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.utils.models.ModelCharacter;
import com.challenge.disneyworld.utils.models.builders.BuilderCharacter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Character;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    @Autowired
    CharacterRepository characterRepository;


    public ResponseEntity<?> createCharacter(Character character){
        Optional<Character> objectControlName = 
        characterRepository.findByName(character.getName());
        
        if(character.getName() == null ||
           character.getName().trim().isEmpty()
        ){
            return new ResponseEntity<>("The name of the character can't be empty",
            HttpStatus.NOT_ACCEPTABLE);
        }

        if(objectControlName.isPresent()){
            return new ResponseEntity<>("Exists character with the same name",
            HttpStatus.NOT_ACCEPTABLE);
        }

        if(character.getBorn_date() == null){
            return new ResponseEntity<>("The birth day of the character can't be empty",
            HttpStatus.NOT_ACCEPTABLE);
        }
        if(character.getHistory() == null){
            return new ResponseEntity<>("The history of the character can't be empty",
            HttpStatus.NOT_ACCEPTABLE);
        }

        characterRepository.save(character);

        return new ResponseEntity<>("Succesfully created",
        HttpStatus.OK);
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
        Period age = Period.between(character.getBorn_date(), LocalDate.now());
        return age.getYears();
    }

    public Optional<Character> getCharacterById(Long id){
        return characterRepository.findById(id);
    }

    public ResponseEntity<?> updateCharacter(Character character,Long id){
        String name = character.getName();
        Optional<Character> objectControlName = characterRepository.findByName(name);
    
        if( objectControlName.isPresent()){
            //Control nombre unico
            return controlUniqueNameAndDate(character, objectControlName.get(), id);
        }else{
            return controlBodyCharacter(character,id);
        }
    
    }

    private ResponseEntity<?> controlUniqueNameAndDate(Character character,
                                Character objectControlName,
                                 Long id){
        
        if(objectControlName.getId() == id){
            //Same id, same name is good
            return controlBodyCharacter(character, id);
        }else{
            return new ResponseEntity<>("Exists character with the same name", 
                                        HttpStatus.CONFLICT);
        }
    }


    private ResponseEntity<?> controlBodyCharacter(Character character, Long id){
        Optional<Character> characterFound = characterRepository.findById(id);
        LocalDate fecha = characterFound.get().getBorn_date();

        if(characterFound.isPresent()){

            if(character.getBorn_date() != null && 
            fecha == character.getBorn_date()){
                return new ResponseEntity<>("You can't update the birth date",
                                        HttpStatus.CONFLICT);
            }else{
                return updateTheCharacter(character,characterFound.get(),id);
            }
        }else{
            return new ResponseEntity<>("Character not found",
            HttpStatus.NOT_FOUND);
        }  

    }

    private ResponseEntity<?> updateTheCharacter(Character character
                                                ,Character characterFound, 
                                                Long id){
        Optional<Character> c = characterRepository.findById(id);
        
        character.setId(c.get().getId());
        character.setBorn_date(c.get().getBorn_date());
        
        if(character.getHistory() == null){
            character.setHistory(c.get().getHistory());
        }

        if(character.getName().trim().isEmpty() ||
           character.getName() == null){
           
           character.setName(c.get().getName());

           characterRepository.save(character);
           return new ResponseEntity<>("Warning the name of the character can't be empty or null. The value of the name is take for default",
                                    HttpStatus.ACCEPTED);
        }else{
            characterRepository.save(character);
            return new ResponseEntity<>("Succesfully updated",
                                    HttpStatus.OK);
        }   
    }

    public ResponseEntity<?> deleteCharacter(Long id){
        Optional<Character> characterFound = characterRepository.findById(id);
        if(characterFound.isPresent()){
           characterRepository.delete(characterFound.get());
           return new ResponseEntity<>("Succesfully deleted",
           HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Character not found",
            HttpStatus.NOT_FOUND);
        }
    }
  

}
