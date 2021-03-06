package com.challenge.disneyworld.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import com.challenge.disneyworld.utils.helpers.Helpers;
import com.challenge.disneyworld.entity.Film;
import com.challenge.disneyworld.dto.ModelDetailAppearance;
import com.challenge.disneyworld.dto.ModelListAppearance;
import com.challenge.disneyworld.dto.builders.BuilderAppearance;
import com.challenge.disneyworld.dto.builders.BuilderDetailAppearances;
import com.challenge.disneyworld.entity.Character;
import com.challenge.disneyworld.entity.Genre;
import com.challenge.disneyworld.entity.Image;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.repository.FilmRepository;
import com.challenge.disneyworld.repository.CharacterRepository;
import com.challenge.disneyworld.repository.GenreRepository;
import com.challenge.disneyworld.repository.ImageRepository;
import com.challenge.disneyworld.service.interfaces.FilmService;
import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class FilmServiceImpl implements FilmService{

    FilmRepository appearanceRepository;
    CharacterRepository characterRepository;
    GenreRepository genreRepository;
    ImageRepository imageRepository;

    @Autowired
    public FilmServiceImpl(FilmRepository appearanceRepository,
                             CharacterRepository characterRepository,
                             GenreRepository genreRepository,
                             ImageRepository imageRepository){

        this.appearanceRepository = appearanceRepository;
        this.characterRepository = characterRepository;
        this.genreRepository = genreRepository;
        this.imageRepository = imageRepository;
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

    private final ResponseEntity<?> responseDateNotAceptable = 
    new ResponseEntity<>("The format of date is incorrect. Format : YYYY-MM-dd",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseImageNotAceptable = 
    new ResponseEntity<>("The images uploaded not exist.",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseGenreNoExists = 
    new ResponseEntity<>("The genre not exists",
    HttpStatus.NOT_FOUND);

    public ResponseEntity<?> createAppearance(Film appearance,
                                              ArrayList<PostImage> postImage,
                                              ProfileImage image){
        if(controlEmptyFields(appearance)) return responseFieldsEmpty;

        if(!Helpers.controlDate(appearance.getCreation_date().toString())) 
        return responseDateNotAceptable;
        
        if(controlTitleUnique(appearance)) return responseTitleMovieSerieNotUnique;

        if(controlCharacters(appearance)) return responseCharacterNoExists;

        if(controlGenre(appearance)) return responseGenreNoExists;

        
        appearance.setHistory(appearance.getHistory().trim());
        appearance.setProfileimage(image);
        appearance.setPostImage(postImage);
        
        appearanceRepository.save(appearance);
        return new ResponseEntity<>("Succesfully created", HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> updateAppearance(Film appearance,Long id,
                                              ArrayList<PostImage> postImage,
                                              ProfileImage image){
        Film requestAppearance = appearanceRepository.findById(id).get();
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

            if(controlUpdateImages(appearance, requestAppearance)) 
            return responseImageNotAceptable;

            if(image != null){
                if(requestAppearance.getProfileimage() != null)
                imageRepository.delete(requestAppearance.getProfileimage());
                
                appearance.setProfileimage(image);
            }else{
                imageProfile(appearance, requestAppearance);
            }

            if(postImage != null){
                appearance.addImages(postImage);
            }

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

    private void imageProfile(Film appearanceUpdate,
                              Film requestAppearance){
        if(appearanceUpdate.getProfileimage() != null){
            Optional<Image> imageRequest = imageRepository.findById(
                            appearanceUpdate.getProfileimage().getId());
            if(appearanceUpdate.getProfileimage() != null){
                if(imageRequest.isPresent())
                requestAppearance.setProfileimage((ProfileImage) imageRequest.get());
            }else{
                imageRepository.delete(imageRequest.get());
                requestAppearance.setProfileimage(null);
            }                         
        }
    }

    private Boolean controlUpdateImages(Film appearanceUpdate,
    Film requestAppearance){

        if(appearanceUpdate.getPostImage().size() == 0 || 
           appearanceUpdate.getPostImage() == null){
            for (PostImage element : requestAppearance.getPostImage()) {
                imageRepository.delete(element);
            }
            return false;
        }else{
            ArrayList<PostImage> images = getAllImages(appearanceUpdate);
            Boolean valid = controlImages(images, requestAppearance);
            if(valid) return true;
            if(images != null){
                for (PostImage element : requestAppearance.getPostImage()){
                   if(!images.contains(element))imageRepository.delete(element);
                }
            }
            images = obtainImages(requestAppearance);
            appearanceUpdate.setPostImage(images);
        }
        return false;
    }

    private Boolean controlImages(ArrayList<PostImage> images, 
                                  Film requestAppearance){
        int count = 0;
        if(images == null) return true;
        for (PostImage element : images) {
            if(!requestAppearance.getPostImage().contains(element)){
                count++;
            }
        }
        if(count > 0){
            for (PostImage element : images) {
                imageRepository.delete(element);
            }
            return true;
        }
        return false;
    }

    private ArrayList<PostImage> obtainImages(Film apperanceUpdate){
        ArrayList<PostImage> images = new ArrayList<PostImage>();
        for (PostImage element : apperanceUpdate.getPostImage()) {
            Optional<Image> imageRequest = imageRepository.findById(element.getId());
            if(imageRequest.isPresent()){
                images.add((PostImage) imageRequest.get());
            }
        }
        return images;
    }

    private ArrayList<PostImage> getAllImages(Film apperanceUpdate){
        ArrayList<PostImage> images = new ArrayList<PostImage>();
        for (PostImage element : apperanceUpdate.getPostImage()) {
            Optional<Image> imageRequest = imageRepository.findById(element.getId());
            if(imageRequest.isPresent()){
                images.add((PostImage) imageRequest.get());
            }else{
                images.add(null);
            }
        }
        if(images.contains(null))images = null;
        return images;
    }

    @Transactional
    public ResponseEntity<?> deleteAppearance(Long id){
        Optional<Film> appearanceRequest = appearanceRepository.findById(id);

        if(appearanceRequest.isPresent()){

            removeFromListCharacters(appearanceRequest.get());
            removeImages(appearanceRequest.get());
            appearanceRepository.delete(appearanceRequest.get());
        }else{
            return responseAppearanceNoExists;
        }
        return new ResponseEntity<>("Succesfully deleted", HttpStatus.OK); 
    }

    private void removeFromListCharacters(Film appearanceRequest){
        
        for (Character element : appearanceRequest.getCharacters()) {
            element.getAppearances().remove(appearanceRequest);    
        }
    }

    private void removeImages(Film appearanceRequest){
        if(appearanceRequest.getProfileimage() != null)
        imageRepository.delete(appearanceRequest.getProfileimage());
        
        if(appearanceRequest.getPostImage().size() > 0){
            for (PostImage image : appearanceRequest.getPostImage()) {
                imageRepository.delete(image);
            }
        }
    }


    private Boolean controlGenre(Film appearance){
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

    private void controlCalification(Film appearance, int califRequest){

        if(appearance.getCalification() < 1 || appearance.getCalification() > 5){
            appearance.setCalification(califRequest);
        }
    }

    private Boolean controlCharacters(Film appearance){
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

    private ArrayList<ModelListAppearance> constructorSeriesOrMovies(ArrayList<Film> lAppearances){
        BuilderAppearance builder = new BuilderAppearance();
        ArrayList<ModelListAppearance> requestAppearance = new ArrayList<ModelListAppearance>();

        for (Film movie : lAppearances) {
            requestAppearance.add(
                builder.setTitle(movie.getTitle()) 
                       .setCreation_Date(movie.getCreation_date())
                       .setImage(movie.getProfileimage())
                       .modelListAppearance()
            );
        }
        return requestAppearance;
    }

    private Boolean controlTitleUnique(Film appearance){
        Optional<Film> objectControlTitle = 
        appearanceRepository.findByTitle(appearance.getTitle());
        return (objectControlTitle.isPresent())?true:false;
    }

    private Boolean controlEmptyFields(Film appearance) {
        return (
           appearance.getTitle() == null ||
           appearance.getTitle().trim().isEmpty() || 
           appearance.getCreation_date() == null || 
           appearance.getHistory() == null || appearance.getHistory().trim().isEmpty() ||
           appearance.getType() == null
           )? true : false;
    }

    /*Querys*/ 
    public ResponseEntity<?> getAppearanceByNameByMovies(String title){

        ArrayList<Film> appearanceByName = 
        appearanceRepository.findByTitleIgnoreCaseContaining(title.trim());
                
        List<Film> apperanceRequest = appearanceByName.stream()
        .filter(a -> a.getTitle().toUpperCase().trim()
                     .equals(title.toUpperCase().trim()))
        .filter(a -> a.getType() == EnumTypeAppearance.MOVIE)
        .collect(Collectors.toList());

        return new ResponseEntity<>(
            constructorSeriesOrMovies((ArrayList<Film>) apperanceRequest)
            ,HttpStatus.OK);
    }

    public ResponseEntity<?> getAppearanceByGenreByMovies(Long idGenre){
        ArrayList<Film> appearances = 
        (ArrayList<Film>) appearanceRepository.findAll();
        
        List<Film> apperanceRequest = appearances.stream()
        .filter(a -> a.getGenre() != null)
        .filter(a -> a.getType() == EnumTypeAppearance.MOVIE)
        .filter(a -> a.getGenre().getId() == idGenre)
        .collect(Collectors.toList());

        return new ResponseEntity<>(
            constructorSeriesOrMovies((ArrayList<Film>) apperanceRequest)
            ,HttpStatus.OK);
    }

    public ResponseEntity<?> getAppearanceOrderByASCByMovies(){
        ArrayList<Film> apperances = 
        appearanceRepository.findAllByOrderByCreationAsc();

        List<Film> apperanceRequest = apperances.stream()
        .filter(a -> a.getType() == EnumTypeAppearance.MOVIE)
        .collect(Collectors.toList());

        return new ResponseEntity<>(
            constructorSeriesOrMovies((ArrayList<Film>) apperanceRequest)
            ,HttpStatus.OK);
    }

    public ResponseEntity<?> getAppearanceOrderByDESCByMovies(){
        ArrayList<Film> apperances = 
        appearanceRepository.findAllByOrderByCreationDesc();

        List<Film> apperanceRequest = apperances.stream()
        .filter(a -> a.getType() == EnumTypeAppearance.MOVIE)
        .collect(Collectors.toList());

        return new ResponseEntity<>(
            constructorSeriesOrMovies((ArrayList<Film>) apperanceRequest)
            ,HttpStatus.OK);
    }
    
    public ResponseEntity<?> getAppearanceByNameBySeries(String title){

        ArrayList<Film> appearanceByName = 
        appearanceRepository.findByTitleIgnoreCaseContaining(title.trim());
                
        List<Film> apperanceRequest = appearanceByName.stream()
        .filter(a -> a.getTitle().toUpperCase().trim()
                     .equals(title.toUpperCase().trim()))
        .filter(a -> a.getType() == EnumTypeAppearance.SERIE)
        .collect(Collectors.toList());

        return new ResponseEntity<>(
            constructorSeriesOrMovies((ArrayList<Film>) apperanceRequest)
            ,HttpStatus.OK);
    }

    public ResponseEntity<?> getAppearanceByGenreBySeries(Long idGenre){
        ArrayList<Film> appearances = 
        (ArrayList<Film>) appearanceRepository.findAll();
        
        List<Film> apperanceRequest = appearances.stream()
        .filter(a -> a.getGenre() != null)
        .filter(a -> a.getType() == EnumTypeAppearance.SERIE)
        .filter(a -> a.getGenre().getId() == idGenre)
        .collect(Collectors.toList());

        return new ResponseEntity<>(
            constructorSeriesOrMovies((ArrayList<Film>) apperanceRequest)
            ,HttpStatus.OK);
    }

    public ResponseEntity<?> getAppearanceOrderByASCBySeries(){
        ArrayList<Film> apperances = 
        appearanceRepository.findAllByOrderByCreationAsc();

        List<Film> apperanceRequest = apperances.stream()
        .filter(a -> a.getType() == EnumTypeAppearance.SERIE)
        .collect(Collectors.toList());

        return new ResponseEntity<>(
            constructorSeriesOrMovies((ArrayList<Film>) apperanceRequest)
            ,HttpStatus.OK);
    }

    public ResponseEntity<?> getAppearanceOrderByDESCBySeries(){
        ArrayList<Film> apperances = 
        appearanceRepository.findAllByOrderByCreationDesc();
        
        List<Film> apperanceRequest = apperances.stream()
        .filter(a -> a.getType() == EnumTypeAppearance.SERIE)
        .collect(Collectors.toList());

        return new ResponseEntity<>(
            constructorSeriesOrMovies((ArrayList<Film>) apperanceRequest)
            ,HttpStatus.OK);
    }
    
    @Transactional
    public ResponseEntity<?> getMovies(){
        ArrayList<Film> listMovies = 
        appearanceRepository.findAllByType(EnumTypeAppearance.MOVIE);
        return (listMovies.size() > 0)?new ResponseEntity<>(constructorSeriesOrMovies(listMovies), HttpStatus.OK):
        new ResponseEntity<>("No exists movies", HttpStatus.NOT_FOUND);
    }

    @Transactional
    public ResponseEntity<?> getSeries(){
        ArrayList<Film> listSeries = 
        appearanceRepository.findAllByType(EnumTypeAppearance.SERIE);
        return new ResponseEntity<>(constructorSeriesOrMovies(listSeries), HttpStatus.OK);
    }

    @Transactional
    public ResponseEntity<?> getAppearanceById(Long id){
        Optional<Film> appearances = appearanceRepository.findById(id);
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
                                   .setProfileImage(appearances.get().getProfileimage())
                                   .setGenre(appearances.get().getGenre())
                                   .setPostImage(appearances.get().getPostImage())
                                   .modelDetailAppearance();
        return new ResponseEntity<>(requestAppearance, HttpStatus.OK);
        }else{
        return responseAppearanceNoExists;
        }
    }



}