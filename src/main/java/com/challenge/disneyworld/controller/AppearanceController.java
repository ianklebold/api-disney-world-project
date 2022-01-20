package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.service.AppearanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping
    public ResponseEntity<?> createAppearance(@RequestBody Appearance appearance){
        return appearanceService.createAppearance(appearance);
    }

    @GetMapping("/movies")
    public ResponseEntity<?> getMovies(){
        return appearanceService.getMovies();
    }

    @GetMapping("/all")
    public ResponseEntity<?> getall(){
        return appearanceService.getall();
    }

    @GetMapping("/series")
    public ResponseEntity<?> getSeries(){
        return appearanceService.getSeries();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppearance(@PathVariable(name = "id") Long id, @RequestBody Appearance appearance){
        return appearanceService.updateAppearance(appearance, id);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppearanceById(@PathVariable(name = "id") Long id){
        return appearanceService.getAppearanceById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppearance(@PathVariable(name = "id") Long id ){
        return appearanceService.deleteAppearance(id);
    }


}
