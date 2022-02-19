package com.challenge.disneyworld.controller;

import java.net.URISyntaxException;
import java.util.ArrayList;

import javax.transaction.Transactional;

import com.challenge.disneyworld.entity.Film;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.service.FilmServiceImpl;
import com.challenge.disneyworld.service.FileUploadService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/appearance")
public class FilmController {

    @Autowired
    FilmServiceImpl appearanceService;

    @Autowired
    FileUploadService fileUploadService;

    @PostMapping(consumes=MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createAppearance(
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="appearance", required=true) Film appearance)
        throws URISyntaxException{
        
        ArrayList<PostImage> postImages = new ArrayList<PostImage>();
        postImages = fileUploadService.uploadImagePostToDB(postImage);
        ProfileImage profileImage = new ProfileImage();
        profileImage = fileUploadService.uploadImageProfileToDB(image);
        
        return appearanceService.createAppearance(appearance,postImages,profileImage);
    }

    @Transactional
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppearance(
        @PathVariable(name = "id") Long id,
        @RequestPart(value="profileImage",required=false) MultipartFile image,
        @RequestPart(value="postImages",required=false)  ArrayList<MultipartFile> postImage,
        @RequestPart(value="appearance", required=true) Film appearance){

        ArrayList<PostImage> postImages = new ArrayList<PostImage>();
        postImages = fileUploadService.uploadImagePostToDB(postImage);
        ProfileImage profileImage = new ProfileImage();
        profileImage = fileUploadService.uploadImageProfileToDB(image);

        return appearanceService.updateAppearance(appearance, id,postImages,profileImage);
    }

    @Transactional
    @GetMapping("/movies")
    public ResponseEntity<?> getMovies(
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="genre", required = false) String idGenre,
        @RequestParam(value="order", required = false) String order
    ){
        if(name != null) return appearanceService.getAppearanceByNameByMovies(name);
        
        if(idGenre != null) return 
        appearanceService.getAppearanceByGenreByMovies(Long.parseLong(idGenre));

        if(order != null){
            if(order.toUpperCase().equals("ASC")) return 
            appearanceService.getAppearanceOrderByASCByMovies();

            if(order.toUpperCase().equals("DESC")) return 
            appearanceService.getAppearanceOrderByDESCByMovies();
        }

        return appearanceService.getMovies();
    }

    @Transactional
    @GetMapping("/series")
    public ResponseEntity<?> getSeries(
        @RequestParam(value="name", required = false) String name,
        @RequestParam(value="genre", required = false) String idGenre,
        @RequestParam(value="order", required = false) String order
    ){
        if(name != null) return appearanceService.getAppearanceByNameBySeries(name);
        
        if(idGenre != null) return 
        appearanceService.getAppearanceByGenreBySeries(Long.parseLong(idGenre));

        if(order != null){
            if(order.toUpperCase().equals("ASC")) return 
            appearanceService.getAppearanceOrderByASCBySeries();

            if(order.toUpperCase().equals("DESC")) return 
            appearanceService.getAppearanceOrderByDESCBySeries();
        }

        return appearanceService.getSeries();
    }

    @Transactional
    @GetMapping("/{id}")
    public ResponseEntity<?> getAppearanceById(@PathVariable(name = "id") Long id){
        return appearanceService.getAppearanceById(id);
    }

    @Transactional
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAppearance(@PathVariable(name = "id") Long id ){
        return appearanceService.deleteAppearance(id);
    }


}
