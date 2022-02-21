package com.challenge.disneyworld.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;


import com.challenge.disneyworld.entity.Film;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.service.FilmServiceImpl;
import com.challenge.disneyworld.service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
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
@RequestMapping("/appearance")
public class FilmController {

    @Autowired
    FilmServiceImpl appearanceService;

    @Autowired
    FileUploadService fileUploadService;

    @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAppearance(
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="appearance", required=true) Film appearance)
        throws URISyntaxException{
        
        ArrayList<PostImage> postImages = new ArrayList<PostImage>();
        postImages = fileUploadService.uploadImagePostToDB(postImage);
        ProfileImage profileImage = new ProfileImage();
        profileImage = fileUploadService.uploadImageProfileToDB(image);
        
        ResponseEntity<?> response = 
        appearanceService.createAppearance(appearance,postImages,profileImage);    
        
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppearance(
        @PathVariable(name = "id") Long id,
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="appearance", required=true) Film appearance){

        ArrayList<PostImage> postImages = new ArrayList<PostImage>();
        postImages = fileUploadService.uploadImagePostToDB(postImage);
        ProfileImage profileImage = new ProfileImage();
        profileImage = fileUploadService.uploadImageProfileToDB(image);

        ResponseEntity<?> response = 
        appearanceService.updateAppearance(appearance, id,postImages,profileImage);        
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @GetMapping("/movies")
    public ResponseEntity<?> getMovies(
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="genre", required = false) String idGenre,
        @RequestParam(value="order", required = false) String order
    ){
        ResponseEntity<?> response;

        if(name != null){
            response = appearanceService.getAppearanceByNameByMovies(name);
            return new ResponseEntity<>(response.getBody(),response.getStatusCode());
        }  
        
        if(idGenre != null){
            response = 
            appearanceService.getAppearanceByGenreByMovies(Long.parseLong(idGenre));
            return new ResponseEntity<>(response.getBody(),response.getStatusCode());
        }
        
        if(order != null){
            if(order.toUpperCase().equals("ASC")){
                response = appearanceService.getAppearanceOrderByASCByMovies();
                return new ResponseEntity<>(response.getBody(),response.getStatusCode());
            }  
            
            if(order.toUpperCase().equals("DESC")){
                response = appearanceService.getAppearanceOrderByDESCByMovies();
                return new ResponseEntity<>(response.getBody(),response.getStatusCode());
            }  
            
        }
        response = appearanceService.getMovies();
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @GetMapping("/series")
    public ResponseEntity<?> getSeries(
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="genre", required = false) String idGenre,
        @RequestParam(value="order", required = false) String order
    ){
        ResponseEntity<?> response;

        if(name != null){
            response = appearanceService.getAppearanceByNameBySeries(name);
            return new ResponseEntity<>(response.getBody(),response.getStatusCode());
        }  
        
        if(idGenre != null){
            response = 
            appearanceService.getAppearanceByGenreBySeries(Long.parseLong(idGenre));
            return new ResponseEntity<>(response.getBody(),response.getStatusCode());
        }
        
        if(order != null){
            if(order.toUpperCase().equals("ASC")){
                response = appearanceService.getAppearanceOrderByASCBySeries();
                return new ResponseEntity<>(response.getBody(),response.getStatusCode());
            }  
            
            if(order.toUpperCase().equals("DESC")){
                response = appearanceService.getAppearanceOrderByDESCBySeries();
                return new ResponseEntity<>(response.getBody(),response.getStatusCode());
            }  
            
        }
        response = appearanceService.getSeries();
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppearanceById(@PathVariable(name = "id") Long id){
        ResponseEntity<?> response = appearanceService.getAppearanceById(id);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppearance(@PathVariable(name = "id") Long id ){
        ResponseEntity<?> response = appearanceService.deleteAppearance(id);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }


}
