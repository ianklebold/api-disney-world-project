package com.challenge.disneyworld.service;

import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.utils.helpers.Helpers;
import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.entity.Character;
import com.challenge.disneyworld.entity.Genre;
import com.challenge.disneyworld.repository.AppearanceRepository;
import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.repository.GenreRepository;
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

    AppearanceRepository appearanceRepository;
    CharacterRepository characterRepository;
    GenreRepository genreRepository;

    @Autowired
    public AppearanceService(AppearanceRepository appearanceRepository,
                             CharacterRepository characterRepository,
                             GenreRepository genreRepository){

        this.appearanceRepository = appearanceRepository;
        this.characterRepository = characterRepository;
        this.genreRepository = genreRepository;
    }

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

    private final ResponseEntity<?> responseCalificationNotAceptable = 
    new ResponseEntity<>("The range of the calification is 1 to 5",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseDateNotAceptable = 
    new ResponseEntity<>("The format of date is incorrect. Format : YYYY-MM-dd",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseGenreNoExists = 
    new ResponseEntity<>("The genre not exists",
    HttpStatus.NOT_FOUND);

    public ResponseEntity<?> createAppearance(Appearance appearance){
        if(controlEmptyFields(appearance)) return responseFieldsEmpty;

        if(!Helpers.controlDate(appearance.getCreation_date().toString())) 
        return responseDateNotAceptable;
        
        if(controlTitleUnique(appearance)) return responseTitleMovieSerieNotUnique;

        if(controlCharacters(appearance)) return responseCharacterNoExists;

        if(controlGenre(appearance)) return responseGenreNoExists;


        appearance.setHistory(appearance.getHistory().trim());
        
        appearanceRepository.save(appearance);
        return new ResponseEntity<>("Succesfully created", HttpStatus.OK);
    }

    public ResponseEntity<?> updateAppearance(Appearance appearance, Long id){
        Appearance requestAppearance = appearanceRepository.findById(id).get();
        if(requestAppearance != null){
            appearance.setId(id);
            if(appearance.getTitle() != null){
                if(controlTitleUnique(appearance)) return responseTitleMovieSerieNotUnique;
            }else{
                appearance.setTitle(requestAppearance.getTitle());
            }

            if(appearance.getCreation_date() != null){
                if(!Helpers.controlDate(appearance.getCreation_date().toString())) 
                return responseDateNotAceptable;
            }else{
                appearance.setCreation_date(requestAppearance.getCreation_date());
            }

            controlCalification(appearance,requestAppearance.getCalification());


            if(appearance.getHistory() == null || appearance.getHistory().trim().isEmpty()){
                appearance.setHistory(requestAppearance.getHistory());
            }

            /*
            TODO CONTROL IMAGENES
            */
            if(appearance.getType() == null){
                appearance.setType(requestAppearance.getType());
            }
            
            if(appearance.getGenre() == null){
                appearance.setGenre(requestAppearance.getGenre());
            }else{
                if(controlGenre(appearance)) return responseGenreNoExists;
            }

            if(controlCharacters(appearance)) return responseCharacterNoExists;
            appearanceRepository.save(appearance);
            return new ResponseEntity<>("Succesfully updated", HttpStatus.OK);
        }else{
            return responseAppearanceNoExists;
        }


    }

    public ResponseEntity<?> deleteAppearance(Long id){
        Optional<Appearance> appearanceRequest = appearanceRepository.findById(id);

        if(appearanceRequest.isPresent()){

            appearanceRepository.delete(appearanceRequest.get());
        }else{
            return responseAppearanceNoExists;
        }
        return new ResponseEntity<>("Succesfully deleted", HttpStatus.OK); 
    }


    private Boolean controlGenre(Appearance appearance){
        if(appearance.getGenre() != null){
            Optional<Genre> genere = genreRepository.findById(appearance.getGenre().getId());
            if(genere.isPresent()){
                appearance.setGenre(genere.get());
                return false;
            }else{
                return true;
            }
        }else{
            return false;
        }
    }

    private void controlCalification(Appearance appearance, int califRequest){

        if(appearance.getCalification() < 1 || appearance.getCalification() > 5){
            appearance.setCalification(califRequest);
        }
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
           appearance.getHistory() == null || appearance.getHistory().trim().isEmpty() ||
           appearance.getType() == null
           )? true : false;
    }




}