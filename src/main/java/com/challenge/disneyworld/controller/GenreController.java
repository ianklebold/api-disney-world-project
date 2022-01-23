package com.challenge.disneyworld.controller;

import java.util.ArrayList;

import com.challenge.disneyworld.entity.Genre;
import com.challenge.disneyworld.service.GenreService;
import com.challenge.disneyworld.utils.models.ModelCrudGenre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disneyworld/api/v1/genres")
public class GenreController {
    @Autowired
    GenreService genreService;

    @PostMapping
    public ResponseEntity<?> createGenre(@RequestBody ModelCrudGenre genreCrud){
        
        return genreService.createGenre(genreCrud);
    }

    @GetMapping("/all")
    public ArrayList<Genre> findAllGenres(){
        
        return genreService.findAllGenres();
    }

    
}
