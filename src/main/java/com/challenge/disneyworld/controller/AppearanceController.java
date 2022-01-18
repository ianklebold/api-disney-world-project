package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.service.AppearanceService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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


}
