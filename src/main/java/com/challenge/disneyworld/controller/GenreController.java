package com.challenge.disneyworld.controller;

import java.net.URISyntaxException;

import com.challenge.disneyworld.dto.ModelCrudGenre;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.service.FileUploadService;
import com.challenge.disneyworld.service.GenreServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/api/v1/genres")
public class GenreController {
    @Autowired
    GenreServiceImpl genreService;
    
    @Autowired
    FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<?> createGenre(
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="genre", required=true) ModelCrudGenre genreCrud)
        throws URISyntaxException{
        
        ProfileImage profileImage = fileUploadService.uploadImageProfileToDB(image);
        
        ResponseEntity<?> response = genreService.createGenre(genreCrud,profileImage);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@RequestBody ModelCrudGenre genreCrud,
                                         @PathVariable(name = "id") Long id){
        
        ResponseEntity<?> response = genreService.updateGenre(genreCrud, id);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable(name = "id") Long id){
        
        ResponseEntity<?> response = genreService.deleteGenre(id);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }
    
    @GetMapping("/all")
    public ResponseEntity<?> findAllGenres(){
        
        ResponseEntity<?> response = genreService.findAllGenres();
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> findGenreById(@PathVariable(name = "id") Long id){
        
        ResponseEntity<?> response = genreService.findGenreById(id);
        return new ResponseEntity<>(response.getBody(),response.getStatusCode());
    }

    
}
