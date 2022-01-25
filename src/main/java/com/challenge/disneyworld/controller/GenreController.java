package com.challenge.disneyworld.controller;

import java.net.URISyntaxException;

import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.service.FileUploadService;
import com.challenge.disneyworld.service.GenreService;
import com.challenge.disneyworld.utils.models.ModelCrudGenre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
@RequestMapping("/disneyworld/api/v1/genres")
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

    @PostMapping(value="/prueba", consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> pruebaGnre(
           @RequestPart(value="files",required=false) MultipartFile files[],
           @RequestPart(value="genre", required=true) ModelCrudGenre genreCrud)
           throws URISyntaxException{
        
        System.out.println("Funciona perefecto");
        System.out.println(files[0].getName());
        System.out.println(genreCrud.getName());
        return new ResponseEntity<>("Succesfully Created !",
        HttpStatus.OK);
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

    @GetMapping("/all")
    public ResponseEntity<?> findAllGenres(){
        
        return genreService.findAllGenres();
    }

    
}
