package com.challenge.disneyworld.service.interfaces;

import com.challenge.disneyworld.dto.ModelCrudGenre;
import com.challenge.disneyworld.entity.ProfileImage;

import org.springframework.http.ResponseEntity;

public interface GenreService {
    public ResponseEntity<?> createGenre(ModelCrudGenre genreCRUD, 
    ProfileImage profileImage);

    public ResponseEntity<?> updateGenre(ModelCrudGenre genreCRUD, Long id);
    public ResponseEntity<?> deleteGenre(Long id);

    public ResponseEntity<?> findAllGenres();
    public  ResponseEntity<?> findGenreById(Long id);
}
