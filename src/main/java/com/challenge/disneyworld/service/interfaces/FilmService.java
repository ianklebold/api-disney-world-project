package com.challenge.disneyworld.service.interfaces;


import java.util.ArrayList;

import com.challenge.disneyworld.entity.Film;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;

import org.springframework.http.ResponseEntity;

public interface FilmService {
    public ResponseEntity<?> createAppearance(Film appearance,
    ArrayList<PostImage> postImage,
    ProfileImage image);

    public ResponseEntity<?> updateAppearance(Film appearance,Long id,
    ArrayList<PostImage> postImage,
    ProfileImage image);

    public ResponseEntity<?> deleteAppearance(Long id);

    public ResponseEntity<?> getMovies();
    public ResponseEntity<?> getSeries();
    public ResponseEntity<?> getAppearanceById(Long id);
    public ResponseEntity<?> getAppearanceByNameByMovies(String title);
    public ResponseEntity<?> getAppearanceByGenreByMovies(Long idGenre);
    public ResponseEntity<?> getAppearanceOrderByASCByMovies();
    public ResponseEntity<?> getAppearanceOrderByDESCByMovies();
    public ResponseEntity<?> getAppearanceByNameBySeries(String title);
    public ResponseEntity<?> getAppearanceByGenreBySeries(Long idGenre);
    public ResponseEntity<?> getAppearanceOrderByASCBySeries();
    public ResponseEntity<?> getAppearanceOrderByDESCBySeries();
}
