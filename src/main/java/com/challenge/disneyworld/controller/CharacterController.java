package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.service.CharacterService;
import com.challenge.disneyworld.service.FileUploadService;

import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.transaction.Transactional;

import com.challenge.disneyworld.entity.Character;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/characters")
public class CharacterController {
    @Autowired
    CharacterService characterService;

    @Autowired
    FileUploadService fileUploadService;

    @Transactional
    @PostMapping()
    public ResponseEntity<?> createCharacter(
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="character", required=true) Character character)
        throws URISyntaxException{
        
        ResponseEntity<?> controlCharacter = characterService.controlCharacter(character);
        
        if(controlCharacter != null) return controlCharacter;
        
        ArrayList<PostImage> postImages = new ArrayList<PostImage>();
        postImages = fileUploadService.uploadImagePostToDB(postImage);
        ProfileImage profileImage = new ProfileImage();
        profileImage = fileUploadService.uploadImageProfileToDB(image);
        return characterService.createCharacter(postImages,profileImage,character);
        
    } 

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacter(
        @PathVariable(name = "id") Long id,
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="character", required=true) Character character){
    
        ResponseEntity<?> controlCharacter = 
        characterService.updateControlCharacter(character,id);
        if(controlCharacter != null) return controlCharacter;
        try {
            ArrayList<PostImage> postImages = new ArrayList<PostImage>();
            postImages = fileUploadService.uploadImagePostToDB(postImage);
            ProfileImage profileImage = new ProfileImage();
            profileImage = fileUploadService.uploadImageProfileToDB(image);
            return characterService.updateCharacter(postImages,profileImage,character);
        } catch (Exception e) {
            return new ResponseEntity<>("Character can't updated",
            HttpStatus.NOT_ACCEPTABLE);
        }
        
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacterById(@PathVariable(name = "id") Long id){
        return characterService.getCharacterById(id);
    }
/*
    @Transactional
    @GetMapping
    public ResponseEntity<?> getCharacter(){
        return characterService.getCharacter();
    }
*/
    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable(name = "id") Long id){
        return characterService.deleteCharacter(id);
    }

    @Transactional
    @GetMapping
    public ResponseEntity<?> getCharacterByName(
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="age", required = false) String age,
        @RequestParam(value="movies", required = false) String idAppearance){
        
        if(name != null) return characterService.getCharacterByName(name);

        try {
            if(age != null) 
            return characterService.getCharacterByAge(Integer.parseInt(age));
        } catch (Exception e) {
            return new ResponseEntity<>("The param input is not a number",
            HttpStatus.NOT_ACCEPTABLE);
        }

        try {
            if(idAppearance != null) 
            return characterService.getCharactersByAppearance(Long.parseLong(idAppearance));
        } catch (Exception e) {
           return new ResponseEntity<>("The param input is not a number",
            HttpStatus.NOT_ACCEPTABLE);
        }
        
        return characterService.getCharacter();
    }
}
