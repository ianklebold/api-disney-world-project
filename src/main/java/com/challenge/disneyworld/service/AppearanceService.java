package com.challenge.disneyworld.service;

import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.entity.Character;
import com.challenge.disneyworld.repository.AppearanceRepository;
import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;
import com.challenge.disneyworld.utils.models.ModelDetailAppearance;
import com.challenge.disneyworld.utils.models.ModelListAppearance;
import com.challenge.disneyworld.utils.models.builders.BuilderAppearance;
import com.challenge.disneyworld.utils.models.builders.BuilderDetailAppearances;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class AppearanceService {

    @Autowired
    AppearanceRepository appearanceRepository;

    @Autowired
    CharacterRepository characterRepository;

    private final ResponseEntity<?> responseFieldsEmpty = 
    new ResponseEntity<>("The fields Title, Creation date, History or type can't be empty",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseTitleMovieSerieNotUnique = 
    new ResponseEntity<>("Exists Movie/Serie with the same name",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseAppearanceNoExists = 
    new ResponseEntity<>("The Movie/Serie not exists",
    HttpStatus.NOT_FOUND);

    private final ResponseEntity<?> responseCharacterNoExists = 
    new ResponseEntity<>("One or more characters no exists",
    HttpStatus.NOT_ACCEPTABLE);

    public ResponseEntity<?> createAppearance(Appearance appearance){
        if(controlEmptyFields(appearance)) return responseFieldsEmpty;
        
        if(controlTitleUnique(appearance)) return responseTitleMovieSerieNotUnique;

        if(controlCharacters(appearance)) return responseCharacterNoExists;

        appearanceRepository.save(appearance);



        return new ResponseEntity<>("Succesfully created", HttpStatus.OK);
    }

    private Boolean controlCharacters(Appearance appearance){
        ArrayList<Character> listaCharacters = new ArrayList<Character>();
        for (Character character : appearance.getCharacters()) {
            Optional<Character> characterRequest = 
            characterRepository.findById(character.getId());
            if(characterRequest.isPresent()){
                listaCharacters.add(characterRequest.get());
                
            }else{
                listaCharacters.add(null);
            }
        }
        if(listaCharacters.contains(null)){
            return true;
        }else{
            appearance.setCharacters(listaCharacters);
            return false;
        }
    } 

    public ResponseEntity<?> getMovies(){
        ArrayList<Appearance> listMovies = 
        appearanceRepository.findAllByType(EnumTypeAppearance.MOVIE);
        return (listMovies.size() > 0)?new ResponseEntity<>(constructorSeriesOrMovies(listMovies), HttpStatus.OK):
        new ResponseEntity<>("No exists movies", HttpStatus.NOT_FOUND);
    }

    //TODO DELETE THIS METHOD
    public ResponseEntity<?> getall(){
        ArrayList<Appearance> listMovies = 
        (ArrayList<Appearance>) appearanceRepository.findAll();
        return (listMovies.size() > 0)?new ResponseEntity<>(listMovies, HttpStatus.OK):
        new ResponseEntity<>("No exists appearance", HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<?> getSeries(){
        ArrayList<Appearance> listSeries = 
        appearanceRepository.findAllByType(EnumTypeAppearance.SERIE);
        return new ResponseEntity<>(constructorSeriesOrMovies(listSeries), HttpStatus.OK);
    }

    public ResponseEntity<?> getAppearanceById(Long id){
        Optional<Appearance> appearances = appearanceRepository.findById(id);
        ModelDetailAppearance requestAppearance = new ModelDetailAppearance();
        
        BuilderDetailAppearances builder = new BuilderDetailAppearances();

        if(appearances.isPresent()){
        requestAppearance = builder.setId(appearances.get().getId())
                                   .setTitle(appearances.get().getTitle())
                                   .setHistory(appearances.get().getHistory())
                                   .setCalification(appearances.get().getCalification())
                                   .setType(appearances.get().getType())
                                   .setCreationDate(appearances.get().getCreation_date())
                                   .setListCharacters(appearances.get().getCharacters())
                                   .modelDetailAppearance();
        return new ResponseEntity<>(requestAppearance, HttpStatus.OK);
        }else{
        return responseAppearanceNoExists;
        }
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