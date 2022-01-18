package com.challenge.disneyworld.service;

import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.repository.AppearanceRepository;
import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;
import com.challenge.disneyworld.utils.models.ModelListAppearance;
import com.challenge.disneyworld.utils.models.builders.BuilderAppearance;

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

    public ResponseEntity<?> getMovies(){
        ArrayList<Appearance> listMovies = 
        appearanceRepository.findAllByType(EnumTypeAppearance.MOVIE);
        return (listMovies.size() > 0)?new ResponseEntity<>(constructorSeriesOrMovies(listMovies), HttpStatus.OK):
        new ResponseEntity<>("No exists movies", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getSeries(){
        ArrayList<Appearance> listSeries = 
        appearanceRepository.findAllByType(EnumTypeAppearance.SERIE);
        return new ResponseEntity<>(constructorSeriesOrMovies(listSeries), HttpStatus.OK);
    }

    private ArrayList<ModelListAppearance> constructorSeriesOrMovies(ArrayList<Appearance> lAppearances){
        BuilderAppearance builder = new BuilderAppearance();
        ArrayList<ModelListAppearance> requestAppearance = new ArrayList<ModelListAppearance>();

        for (Appearance movie : lAppearances) {
            requestAppearance.add(
                builder.setTitle(movie.getTitle()) 
                       .setCreation_Date(movie.getCreation_date())
                //     .setImage(movie.getProfileimage())
                       .modelListAppearance()
            );
        }
        return requestAppearance;
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