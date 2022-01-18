package com.challenge.disneyworld.service;

import java.util.Optional;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.repository.AppearanceRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AppearanceService {

    @Autowired
    AppearanceRepository appearanceRepository;

    private final ResponseEntity<?> responseFieldsEmpty = 
    new ResponseEntity<>("The fields Title, Creation date, History or type can't be empty",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseTitleMovieSerieNotUnique = 
    new ResponseEntity<>("Exists Movie/Serie with the same name",
    HttpStatus.NOT_ACCEPTABLE);


    public ResponseEntity<?> createAppearance(Appearance appearance){
        if(controlEmptyFields(appearance)) return responseFieldsEmpty;
        
        if(controlTitleUnique(appearance)) return responseTitleMovieSerieNotUnique;

        appearanceRepository.save(appearance);
        return new ResponseEntity<>("Succesfully created", HttpStatus.OK);
    }

    private Boolean controlTitleUnique(Appearance appearance){
        Optional<Appearance> objectControlTitle = 
        appearanceRepository.findByTitle(appearance.getTitle());
        return (objectControlTitle.isPresent())?true:false;
    }

    private Boolean controlEmptyFields(Appearance appearance) {
        return (
           appearance.getTitle() == null ||
           appearance.getTitle().trim().isEmpty() || 
           appearance.getCreation_date() == null || 
           appearance.getHistory() == null ||
           appearance.getType() == null
           )? true : false;
    }


}