package com.challenge.disneyworld.service;

import com.challenge.disneyworld.repository.AppearanceRepository;
import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.repository.ImageRepository;
import com.challenge.disneyworld.utils.helpers.Helpers;
import com.challenge.disneyworld.utils.models.ModelDetailCharacter;
import com.challenge.disneyworld.utils.models.ModelListCharacter;
import com.challenge.disneyworld.utils.models.builders.BuilderCharacter;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.entity.Character;
import com.challenge.disneyworld.entity.Image;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CharacterService {
    
    CharacterRepository characterRepository;
    AppearanceRepository appearanceRepository;
    ImageRepository imageRepository;

    @Autowired
    public CharacterService(
        CharacterRepository characterRepository, 
        AppearanceRepository appearanceRepository,
        ImageRepository imageRepository){
        this.characterRepository = characterRepository;
        this.appearanceRepository = appearanceRepository;
        this.imageRepository = imageRepository;
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

    private final ResponseEntity<?> responseImageNotAceptable = 
    new ResponseEntity<>("The images uploaded not exist.",
    HttpStatus.NOT_ACCEPTABLE);

    public ResponseEntity<?> createCharacter(ArrayList<PostImage> postImage,
                                             ProfileImage imageCharacter,
                                             Character character){
        character.setProfileimage(imageCharacter);
        character.setPostImage(postImage);
        characterRepository.save(character);
        return new ResponseEntity<>("Succesfully created",
        HttpStatus.OK);
    }

    public ResponseEntity<?> controlCharacter(Character character){
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

        return null;
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



     
    public int returnAge(Character character){
        Period age = Period.between(character.getBorn_date(), LocalDate.now());
        return age.getYears();
    }

    public ResponseEntity<?> updateControlCharacter(Character character,Long id){
        Optional<Character> requestCharacter = characterRepository.findById(id);
        if(requestCharacter.isPresent()){

            ResponseEntity<?> controlName;
            character.setId(requestCharacter.get().getId());
            
            controlName = controlNameCharacter(character, requestCharacter.get());
            if(controlName != null) return controlName;

            if(!Helpers.controlRegexName(character.getName()))
            return new ResponseEntity<>("The name of the character is invalid",
            HttpStatus.NOT_ACCEPTABLE);

            controlName = controlBornDateCharacter(character, requestCharacter.get());
            if(controlName != null) return controlName;

            if(character.getHistory() == null) 
            character.setHistory(requestCharacter.get().getHistory());  

            if(updateListAppearance(character,requestCharacter.get())) 
            return responseAppearanceNoExists;

            return null;
        }else{
            return responseCharacterNoExists;
        }
    }

    public ResponseEntity<?>  updateCharacter(
        ArrayList<PostImage> postImage,
        ProfileImage imageCharacter,
        Character character){

        Character requestCharacter = 
        characterRepository.findById(character.getId()).get();

        if(controlUpdateImages(character, requestCharacter)) 
            return responseImageNotAceptable;

        if(imageCharacter != null){
            if(requestCharacter.getProfileimage() != null)
            imageRepository.delete(requestCharacter.getProfileimage());
            
            character.setProfileimage(imageCharacter);
        }else{
            controlProfileImage(character, requestCharacter);
        }

        if(postImage != null){
            character.addImages(postImage);
        }

        characterRepository.save(character);
        return new ResponseEntity<>("Succesfully updated",HttpStatus.OK);
    }

    private void controlProfileImage(Character character,
                                     Character requestCharacter){
        Optional<Image> imageRequest = imageRepository.findById(
            character.getProfileimage().getId());
        if(character.getProfileimage()!= null){
        if(imageRequest.isPresent())
            requestCharacter.setProfileimage((ProfileImage) imageRequest.get());
        }else{
            imageRepository.delete(imageRequest.get());
            requestCharacter.setProfileimage(null);
        }
    }

    private Boolean controlUpdateImages(Character character,
                                        Character requestCharacter){

        if(character.getPostImage().size() == 0 || 
           character.getPostImage() == null){
            for (PostImage element : requestCharacter.getPostImage()) {
                imageRepository.delete(element);
            }
            return false;
        }else{
            ArrayList<PostImage> images = getAllImages(character);
            Boolean valid = controlImages(images, requestCharacter);
            if(valid) return true;
            if(images != null){
                for (PostImage element : requestCharacter.getPostImage()){
                   if(!images.contains(element))imageRepository.delete(element);
                }
            }
            images = obtainImages(requestCharacter);
            character.setPostImage(images);
        }
        return false;
    }

    private ArrayList<PostImage> obtainImages(Character character){
        ArrayList<PostImage> images = new ArrayList<PostImage>();
        for (PostImage element : character.getPostImage()) {
            Optional<Image> imageRequest = imageRepository.findById(element.getId());
            if(imageRequest.isPresent()){
                images.add((PostImage) imageRequest.get());
            }
        }
        return images;
    }

    private ArrayList<PostImage> getAllImages(Character character){
        ArrayList<PostImage> images = new ArrayList<PostImage>();
        for (PostImage element : character.getPostImage()) {
            Optional<Image> imageRequest = imageRepository.findById(element.getId());
            if(imageRequest.isPresent()){
                images.add((PostImage) imageRequest.get());
            }else{
                images.add(null);
            }
        }
        if(images.contains(null))images = null;
        return images;
    }

    private Boolean controlImages(ArrayList<PostImage> images, 
                                  Character requestCharacter){
        int count = 0;
        if(images == null) return true;
        for (PostImage element : images) {
            if(!requestCharacter.getPostImage().contains(element)){
                count++;
            }
        }
        if(count > 0){
            for (PostImage element : images) {
                imageRepository.delete(element);
            }
            return true;
        }
        return false;
    }

    private ResponseEntity<?> controlNameCharacter(Character character,
                                                   Character requestCharacter){
        if(character.getName() != null){
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
            character.setName(requestCharacter.getName());
        }
        return null;
    }

    private ResponseEntity<?> controlBornDateCharacter(
                             Character character,
                             Character requestCharacter){
        if(character.getBorn_date() == null){
            character.setBorn_date(requestCharacter.getBorn_date());
        }else{
            if(character.getBorn_date() != requestCharacter.getBorn_date()) 
            return dateBornNotChange;
        }
        return null;
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
            character.setAppearances(listAppearances);
            return false;
        }
        
    }

    private Boolean updateListAppearance(Character character, Character characterRequest){
        if(character.getAppearances().size() == 0){
            for (Appearance element : characterRequest.getAppearances()) {
                element.getCharacters().remove(characterRequest);
                appearanceRepository.save(element);
            }
        }else{   
            if(characterRequest.getAppearances().size() != 0){
                //Appearances before update
                LongStream idAppearances = characterRequest.getAppearances().stream()
                                            .mapToLong(appearance -> appearance.getId());
                //New appearances
                LongStream idNewAppearances = character.getAppearances().stream()
                                            .mapToLong(appearance -> appearance.getId());

                ArrayList<Long> listIdNewAppearances = new ArrayList<Long>();

                for (long element : idNewAppearances.toArray()){
                    listIdNewAppearances.add(element);
                }//[1]  [1,3]

                for (long element : idAppearances.toArray()){
                    if(!listIdNewAppearances.contains(element)){
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
                if(!appearanceRequest.isPresent()){
                    return true; //Error exists
                }
            }
            for (Appearance element : character.getAppearances()) {
                Optional<Appearance> appearanceRequest = 
                appearanceRepository.findById(element.getId());
                if(!characterRequest.getAppearances().contains(appearanceRequest.get())){
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

            
           removeFromListAppearances(characterFound.get());
           removeImages(characterFound.get());
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

    private void removeImages(Character characterRequest){
        if(characterRequest.getProfileimage() != null)
        imageRepository.delete(characterRequest.getProfileimage());
        
        if(characterRequest.getPostImage().size() > 0){
            for (PostImage image : characterRequest.getPostImage()) {
                imageRepository.delete(image);
            }
        }
    }


    /*QUERYS*/

    public ResponseEntity<?> getCharacterByName(String name){

        ArrayList<Character> characterByName = 
        characterRepository.findByNameIgnoreCaseContaining(name.trim());
                
        List<Character> characterRequest = characterByName.stream()
        .filter(c -> c.getName().toUpperCase().trim()
                     .equals(name.toUpperCase().trim()))
        .collect(Collectors.toList());

        return returnCharacters(characterRequest);
    }
    public ResponseEntity<?> getCharacterByAge(int age){

        ArrayList<Character> characters = 
        (ArrayList<Character>) characterRepository.findAll();
                
        List<Character> characterRequest = characters.stream()
        .filter(c -> returnAge(c) == age)
        .collect(Collectors.toList());

        return returnCharacters(characterRequest);
    }

    public ResponseEntity<?> getCharactersByAppearance(Long idAppearance){

        Optional<Appearance> appearances = appearanceRepository.findById(idAppearance);
        if(appearances.isPresent()){
            List<Character> characterRequest = appearances.get().getCharacters();
            return returnCharacters(characterRequest);
        }else{
            return new ResponseEntity<>("Appearance not found",
            HttpStatus.NOT_FOUND);
        }        
    }

    private ResponseEntity<?> returnCharacters(List<Character> characterRequest){
        if(characterRequest.size() > 0){
            BuilderCharacter builder = new BuilderCharacter();
            ArrayList<ModelListCharacter> requestCharacters = 
            new ArrayList<ModelListCharacter>();

            for (Character c : characterRequest) {
                    
                requestCharacters.add(
                    builder.setName(c.getName())
                    .setProfileImage(c.getProfileimage())
                    .builderListCharacter()
                );
            } 
            return new ResponseEntity<>(requestCharacters, HttpStatus.OK);  
        }else{
            return new ResponseEntity<>("No exists Characters",
            HttpStatus.NOT_FOUND);
        }
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
                        .setProfileImage(character.get().getProfileimage())
                        .setPostImage(character.get().getPostImage())
                        .builder();
            
            return new ResponseEntity<>(characterRequest, HttpStatus.OK);
         }else{
            return new ResponseEntity<>("Character not found",
            HttpStatus.NOT_FOUND);
         }
 
     }


    public ResponseEntity<?> getCharacter(){
        ArrayList<Character> listCharacters = (ArrayList<Character>) characterRepository.findAll();
        
        return returnCharacters(listCharacters);
    }

}
