package com.challenge.disneyworld.controller;


import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.repository.ImageRepository;
import com.challenge.disneyworld.service.ImageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ImageController {
    @Autowired
    ImageService imageService;

    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/avatar/{id}")
    public ResponseEntity<Resource> findImageProfileById(@PathVariable(name = "id") String id){
        ProfileImage image = (ProfileImage) imageRepository.findById(id).get();
        return imageService.generateImage(image);
    }

    @GetMapping("/postimages/{id}")
    public ResponseEntity<Resource> findImagePostById(@PathVariable(name = "id") String id){
        PostImage image = (PostImage) imageRepository.findById(id).get();
        return imageService.generateImage(image);
    }

}
