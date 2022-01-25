package com.challenge.disneyworld.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.transaction.Transactional;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.service.AppearanceService;
import com.challenge.disneyworld.service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/disneyworld/api/v1/appearance")
public class AppearanceController {
 /*
 Endpoints
            /movies peliculas
            /series series
 */   
    @Autowired
    AppearanceService appearanceService;

    @Autowired
    FileUploadService fileUploadService;

    @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAppearance(
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="appearance", required=true) Appearance appearance)
        throws URISyntaxException{
        
        ArrayList<PostImage> postImages = new ArrayList<PostImage>();
        postImages = fileUploadService.uploadImagePostToDB(postImage);
        ProfileImage profileImage = new ProfileImage();
        profileImage = fileUploadService.uploadImageProfileToDB(image);
        
        return appearanceService.createAppearance(appearance,postImages,profileImage);
    }

    @Transactional
    @GetMapping("/movies")
    public ResponseEntity<?> getMovies(){
        return appearanceService.getMovies();
    }
    
    
    @GetMapping("/all")
    public ResponseEntity<?> getall(){
        return appearanceService.getall();
    }

    @Transactional
    @GetMapping("/series")
    public ResponseEntity<?> getSeries(){
        return appearanceService.getSeries();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppearance(@PathVariable(name = "id") Long id, @RequestBody Appearance appearance){
        return appearanceService.updateAppearance(appearance, id);
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppearanceById(@PathVariable(name = "id") Long id){
        return appearanceService.getAppearanceById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppearance(@PathVariable(name = "id") Long id ){
        return appearanceService.deleteAppearance(id);
    }


}
