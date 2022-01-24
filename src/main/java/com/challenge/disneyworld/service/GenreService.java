package com.challenge.disneyworld.service;

import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Genre;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.repository.GenreRepository;
import com.challenge.disneyworld.utils.helpers.Helpers;
import com.challenge.disneyworld.utils.models.ModelCrudGenre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Control empty fields
 * Control nombre de genero sin espacios a los extremos
 * El nombre de genero debe ser unico
 * Pasarlo a uppercase
 */

@Service
public class GenreService {
    @Autowired
    GenreRepository genreRepository;
    
    private final ResponseEntity<?> responseFieldsEmpty = 
    new ResponseEntity<>("The field name can't be empty",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseFieldIsNotAcceptable = 
    new ResponseEntity<>("The field is'nt accepted",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseFieldIsNotUnique= 
    new ResponseEntity<>("The field name is'nt unique",
    HttpStatus.NOT_ACCEPTABLE);

    private final ResponseEntity<?> responseSuccesfullyCreated= 
    new ResponseEntity<>("Succesfully Created !",
    HttpStatus.OK);

    private final ResponseEntity<?> responseSuccesfullyUpdated= 
    new ResponseEntity<>("Succesfully Updated !",
    HttpStatus.OK);

    private final ResponseEntity<?> responseGenreNotExists= 
    new ResponseEntity<>("The genre is'nt found !",
    HttpStatus.NOT_FOUND);

    private final ResponseEntity<?> responseSuccesfullyDeleted= 
    new ResponseEntity<>("Succesfully Deleted !",
    HttpStatus.OK);

    public ResponseEntity<?> createGenre(ModelCrudGenre genreCRUD, 
                                         ProfileImage profileImage){
       if(controlEmptyField(genreCRUD.getName())) return responseFieldsEmpty;

       if(!Helpers.controlRegexName(genreCRUD.getName()))
       return responseFieldIsNotAcceptable;

       genreCRUD.setName(genreCRUD.getName().trim().toUpperCase());

       if(controlUniqueName(genreCRUD.getName()))return responseFieldIsNotUnique;
       //TODO COMPLETAR IMAGEN
       Genre genre = new Genre();
       genre.setName(genreCRUD.getName());
       genre.setProfileimage(profileImage);
       genreRepository.save(genre);

       return responseSuccesfullyCreated;
    }

    public ResponseEntity<?> updateGenre(ModelCrudGenre genreCRUD, Long id){
        Optional<Genre> genreRequest = genreRepository.findById(id);
        if(genreRequest.isPresent()){
            if(genreCRUD.getName() != null){
                if(!Helpers.controlRegexName(genreCRUD.getName()))
                return responseFieldIsNotAcceptable;

                genreCRUD.setName(genreCRUD.getName().trim().toUpperCase());

                if(controlUniqueName(genreCRUD.getName())){
                    if(genreRepository.findByName(genreCRUD.getName()).get().getId() != id
                    ) return responseFieldIsNotUnique;
                }
                genreRequest.get().setName(genreCRUD.getName());
            }
        
            genreRepository.save(genreRequest.get());
            return responseSuccesfullyUpdated; 
        }else{
            return responseGenreNotExists;
        }
    }

    public ResponseEntity<?> deleteGenre(Long id){
        Optional<Genre> genreRequest = genreRepository.findById(id);
        if(genreRequest.isPresent()){
            genreRepository.delete(genreRequest.get());
            return responseSuccesfullyDeleted;
        }else{
            return responseGenreNotExists;
        }
    }

    public ArrayList<Genre> findAllGenres(){
        return (ArrayList<Genre>) genreRepository.findAll();
    }

    

    private Boolean controlEmptyField(String name){
        return (
            name == null || name.replaceAll("\\s+","").isEmpty()
        )? true : false;
    }

    private Boolean controlUniqueName(String name){
        Optional<Genre> genreRequest = genreRepository.findByName(name);
        return (genreRequest.isPresent())?true:false;
    }
}
