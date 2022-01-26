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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/disneyworld/api/v1/characters")
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
        @RequestPart(value="appearance", required=true) Character character)
        throws URISyntaxException{
        
        ResponseEntity<?> controlCharacter = characterService.controlCharacter(character);
        
        if(controlCharacter != null) return controlCharacter;
        try {
            ArrayList<PostImage> postImages = new ArrayList<PostImage>();
            postImages = fileUploadService.uploadImagePostToDB(postImage);
            ProfileImage profileImage = new ProfileImage();
            profileImage = fileUploadService.uploadImageProfileToDB(image);
            return characterService.createCharacter(postImages,profileImage,character);
        } catch (Exception e) {
            return new ResponseEntity<>("Character can't created",
            HttpStatus.NOT_ACCEPTABLE);
        }
        
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
