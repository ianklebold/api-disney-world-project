package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.service.CharacterServiceImpl;
import com.challenge.disneyworld.service.FileUploadService;

import java.net.URISyntaxException;
import java.util.ArrayList;

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
@RequestMapping("/characters")
public class CharacterController {
    @Autowired
    CharacterServiceImpl characterService;

    @Autowired
    FileUploadService fileUploadService;

    @PostMapping()
    public ResponseEntity<?> createCharacter(
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="character", required=true) Character character)
        throws URISyntaxException{ 
        ArrayList<PostImage> postImages = new ArrayList<PostImage>();
        postImages = fileUploadService.uploadImagePostToDB(postImage);
        ProfileImage profileImage = new ProfileImage();
        profileImage = fileUploadService.uploadImageProfileToDB(image);

        ResponseEntity<?> response = 
        characterService.createCharacter(postImages,profileImage,character);    

        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
        
    } 

    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacter(
        @PathVariable(name = "id") Long id,
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="character", required=true) Character character){

        try {
            ArrayList<PostImage> postImages = new ArrayList<PostImage>();
            postImages = fileUploadService.uploadImagePostToDB(postImage);
            ProfileImage profileImage = new ProfileImage();
            profileImage = fileUploadService.uploadImageProfileToDB(image);
            ResponseEntity<?> response = 
            characterService.updateCharacter(id,postImages,profileImage,character);
            return new ResponseEntity<>(response.getBody(),response.getStatusCode());
            
        } catch (Exception e) {
            return new ResponseEntity<>("Ups Something was wrong.",
            HttpStatus.CONFLICT);
        }
        
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getCharacterById(@PathVariable(name = "id") Long id){
        ResponseEntity<?> response = 
        characterService.getCharacterById(id);

        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable(name = "id") Long id){
        ResponseEntity<?> response = characterService.deleteCharacter(id);

        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @GetMapping
    public ResponseEntity<?> getCharacter(
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="age", required = false) String age,
        @RequestParam(value="movies", required = false) String idAppearance){
        ResponseEntity<?> response;
        
        try {
            if(name != null){
                response = characterService.getCharacterByName(name);
                return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    
            } 
            if(age != null){
                response = characterService.getCharacterByAge(Integer.parseInt(age));
                return new ResponseEntity<>(response.getBody(),response.getStatusCode());
            } 
            if(idAppearance != null){
                response = characterService.
                getCharactersByAppearance(Long.parseLong(idAppearance));
                return new ResponseEntity<>(response.getBody(),response.getStatusCode());
            }
        } catch (Exception e) {
            return new ResponseEntity<>("The param input is not a number",
            HttpStatus.NOT_ACCEPTABLE);
        }
        response = characterService.getCharacter();
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }
}
