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
import java.util.stream.Collectors;
import java.util.stream.LongStream;


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

    private final ResponseEntity<?> dateBornNotChange = 
    new ResponseEntity<>("The Creation date can't be change",
    HttpStatus.NOT_ACCEPTABLE);
 
    private final ResponseEntity<?> responseCharacterNoExists = 
    new ResponseEntity<>("The Character not exists",
    HttpStatus.NOT_FOUND);

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

        if(crearListAppearance(character)) return responseAppearanceNoExists;

        characterRepository.save(character);

        return new ResponseEntity<>("Succesfully created",
        HttpStatus.OK);
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
        Optional<Character> requestCharacter = characterRepository.findById(id);
        if(requestCharacter.isPresent()){
            character.setId(requestCharacter.get().getId());
            
            if(character.getName() != null){
                System.out.println("Tratamiento nombre");
                String name = character.getName();
                Optional<Character> objectControlName = characterRepository.findByName(name);
                if(objectControlName.isPresent()){
                    if(objectControlName.get().getId() != character.getId()){
                        return new ResponseEntity<>("Exists character with the same name",
                        HttpStatus.NOT_ACCEPTABLE);
                    }

                }

                if(character.getName().replaceAll("\\s+","").isEmpty()){
                    return new ResponseEntity<>("The field name can't be empty",
                        HttpStatus.NOT_ACCEPTABLE);
                }

            }else{
                System.out.println("Tratamiento nombre set");
                character.setName(requestCharacter.get().getName());
            }

            if(!Helpers.controlRegexName(character.getName()))
            return new ResponseEntity<>("The name of the character is invalid",
            HttpStatus.NOT_ACCEPTABLE);

            if(character.getBorn_date() == null){
                System.out.println("Tratamiento fecha nulo");
                character.setBorn_date(requestCharacter.get().getBorn_date());
            }else{
                System.out.println("Tratamiento fecha no nulo");
                if(character.getBorn_date() != requestCharacter.get().getBorn_date()) 
                return dateBornNotChange;
            }

            System.out.println("Tratamiento historia");
            if(character.getHistory() == null) 
            character.setHistory(requestCharacter.get().getHistory());  

            System.out.println("Update appearance");
            if(updateListAppearance(character,requestCharacter.get())) 
            return responseAppearanceNoExists;

            System.out.println(character.getAppearances());
            characterRepository.save(character);

            return new ResponseEntity<>("Succesfully updated",
            HttpStatus.OK);
        }else{
            return responseCharacterNoExists;
        }

    }

    private Boolean crearListAppearance(Character character){
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
            System.out.println(listAppearances);
            character.setAppearances(listAppearances);
            return false;
        }
        
    }

    private Boolean updateListAppearance(Character character, Character characterRequest){
        System.out.println("La cantidad de elementos a cargar : "+ character.getAppearances().size());
        if(character.getAppearances().size() == 0){
            System.out.println("Lista vacia");
            for (Appearance element : characterRequest.getAppearances()) {
                element.getCharacters().remove(characterRequest);
                appearanceRepository.save(element);
            }
        }else{   
            System.out.println("Lista no vacia");
            if(characterRequest.getAppearances().size() != 0){
                //Appearances before update
                System.out.println("Hay que eliminar");
                LongStream idAppearances = characterRequest.getAppearances().stream()
                                            .mapToLong(appearance -> appearance.getId());
                //New appearances
                LongStream idNewAppearances = character.getAppearances().stream()
                                            .mapToLong(appearance -> appearance.getId());

                ArrayList<Long> listIdNewAppearances = new ArrayList<Long>();

                for (long element : idNewAppearances.toArray()){
                    listIdNewAppearances.add(element);
                }//[1]  [1,3]
                System.out.println(listIdNewAppearances);

                for (long element : idAppearances.toArray()){
                    if(!listIdNewAppearances.contains(element)){
                        System.out.println("Eliminamos : " + element);
                        Optional<Appearance> appearanceRequest = 
                        appearanceRepository.findById(element);
                        appearanceRequest.get().getCharacters().remove(characterRequest);
                        appearanceRepository.save(appearanceRequest.get());
                    }
                }
            }
            for (Appearance element : character.getAppearances()) {
                Optional<Appearance> appearanceRequest = 
                appearanceRepository.findById(element.getId());
                System.out.println(element.getId() + "Existe");
                if(!appearanceRequest.isPresent()){
                    return true; //Error exists
                }
            }
            for (Appearance element : character.getAppearances()) {
                Optional<Appearance> appearanceRequest = 
                appearanceRepository.findById(element.getId());
                if(!characterRequest.getAppearances().contains(appearanceRequest.get())){
                        System.out.println(element.getId() + " No esta, se agrega a la lista");
                        appearanceRequest.get().getCharacters().add(characterRequest);
                        appearanceRepository.save(appearanceRequest.get());                    
                    }
                }
            }
            return false;
        }


    public ResponseEntity<?> deleteCharacter(Long id){
        Optional<Character> characterFound = characterRepository.findById(id);
        if(characterFound.isPresent()){

            //Remove from list of appearances     
            removeFromListAppearances(characterFound.get());
           characterRepository.delete(characterFound.get());
           return new ResponseEntity<>("Succesfully deleted",
           HttpStatus.OK);
        }else{
            return new ResponseEntity<>("Character not found",
            HttpStatus.NOT_FOUND);
        }
    }


    private void removeFromListAppearances(Character characterRequest){
        
        for (Appearance element : characterRequest.getAppearances()) {
            element.getCharacters().remove(characterRequest);    
        }
    }

    public ArrayList<Character> getAll(){
        return (ArrayList<Character>) characterRepository.findAll();
    }
  

}
