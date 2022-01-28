package com.challenge.disneyworld.controller;

import java.net.URISyntaxException;

import javax.transaction.Transactional;

import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.service.FileUploadService;
import com.challenge.disneyworld.service.GenreService;
import com.challenge.disneyworld.utils.models.ModelCrudGenre;

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
    GenreService genreService;
    
    @Autowired
    FileUploadService fileUploadService;

    @PostMapping
    public ResponseEntity<?> createGenre(
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="genre", required=true) ModelCrudGenre genreCrud)
        throws URISyntaxException{
        
        ProfileImage profileImage = fileUploadService.uploadImageProfileToDB(image);   
        return genreService.createGenre(genreCrud,profileImage);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@RequestBody ModelCrudGenre genreCrud,
                                         @PathVariable(name = "id") Long id){
        
        return genreService.updateGenre(genreCrud, id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGenre(@PathVariable(name = "id") Long id){
        
        return genreService.deleteGenre(id);
    }

    @Transactional
    @GetMapping("/all")
    public ResponseEntity<?> findAllGenres(){
        
        return genreService.findAllGenres();
    }
    
    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<?> findGenreById(@PathVariable(name = "id") Long id){
        
        return genreService.findGenreById(id);
    }

    
}
