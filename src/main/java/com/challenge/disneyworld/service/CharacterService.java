package com.challenge.disneyworld.service;

import com.challenge.disneyworld.repository.AppearanceRepository;
import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.utils.helpers.Helpers;
import com.challenge.disneyworld.utils.models.ModelDetailCharacter;
import com.challenge.disneyworld.utils.models.ModelListCharacter;
import com.challenge.disneyworld.utils.models.builders.BuilderCharacter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.entity.Character;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    
    CharacterRepository characterRepository;
    AppearanceRepository appearanceRepository;
    

    @Autowired
    public CharacterService(CharacterRepository characterRepository, AppearanceRepository appearanceRepository){
        this.characterRepository = characterRepository;
        this.appearanceRepository = appearanceRepository;
    }

    private final ResponseEntity<?> responseFieldsEmpty = 
    new ResponseEntity<>("The fields Title, Creation date, History or type can't be empty",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseAppearanceNoExists = 
    new ResponseEntity<>("The Movie/Serie not exists",
    HttpStatus.NOT_FOUND);

    public ResponseEntity<?> createCharacter(Character character){

        if(controlEmptyFields(character)) return responseFieldsEmpty;

        Optional<Character> objectControlName = 
        characterRepository.findByName(character.getName());

        if(objectControlName.isPresent()){
            return new ResponseEntity<>("Exists character with the same name",
            HttpStatus.NOT_ACCEPTABLE);
        }

        if(!Helpers.controlRegexName(character.getName()))
        return new ResponseEntity<>("The name of the character is invalid",
        HttpStatus.NOT_ACCEPTABLE);

        if(controlAppearance(character)) return responseAppearanceNoExists;

        characterRepository.save(character);

        return new ResponseEntity<>("Succesfully created",
        HttpStatus.OK);
    }

    private Boolean controlAppearance(Character character){
        if(character.getAppearances().size()>0){
            ArrayList<Appearance> listAppearances = new ArrayList<Appearance>();
            for (Appearance appearance : character.getAppearances()) {

                Optional<Appearance> appearanceRequest = 
                appearanceRepository.findById(appearance.getId());

                if(appearanceRequest.isPresent()){
                    listAppearances.add(appearanceRequest.get());
                    
                }else{
                    listAppearances.add(null);
                }
            }
            if(listAppearances.contains(null)){
                return true;
            }else{
                character.setAppearances(listAppearances);
                return false;
            }
        }else{
            return false;
        }
    }

    private Boolean controlEmptyFields(Character character){
        return (
            character.getName() == null || 
            character.getBorn_date() == null ||
            character.getHistory() == null || 
            character.getName().replaceAll("\\s+","").isEmpty() ||
            character.getHistory().replaceAll("\\s+","").isEmpty()
        )? true : false;
    }

    public ResponseEntity<?> getCharacterById(Long id){
        Optional<Character> character = characterRepository.findById(id);
 
        if(character.isPresent()){
            BuilderCharacter builder = new BuilderCharacter();
 
            ModelDetailCharacter characterRequest;
                
            characterRequest = builder.setId(character.get().getId())
                        .setAge(returnAge(character.get()))
                        .setName(character.get().getName())
                        .setHistory(character.get().getHistory())
                        .setAppearances(character.get().getAppearances())
                        .builder();
            
            return new ResponseEntity<>(characterRequest, HttpStatus.OK);
         }else{
            return new ResponseEntity<>("Character not found",
            HttpStatus.NOT_FOUND);
         }
 
     }
 
    public int returnAge(Character character){
        Period age = Period.between(character.getBorn_date(), LocalDate.now());
        return age.getYears();
    }

    public ResponseEntity<?> getCharacter(){
        ArrayList<Character> listCharacters = (ArrayList<Character>) characterRepository.findAll();
        
        if(listCharacters.size() > 0){
            BuilderCharacter builder = new BuilderCharacter();
            
            ArrayList<ModelListCharacter> requestCharacters = new ArrayList<ModelListCharacter>();

            for (Character c : listCharacters) {
                    
                requestCharacters.add(
                    builder.setName(c.getName())
                    .builderListCharacter()
                );
            } 
            return new ResponseEntity<>(requestCharacters, HttpStatus.OK);  
        }else{
            return new ResponseEntity<>("No exists Characters",
            HttpStatus.NOT_FOUND);
        }
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
