package com.challenge.disneyworld.controller;

import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/disneyworld/api/v1")
public class ImageController {
    @Autowired
    ImageRepository imageRepository;

    @GetMapping("/avatar/{id}")
    public ResponseEntity<Resource> findImageProfileById(@PathVariable(name = "id") String id){
        ProfileImage profileImage = (ProfileImage) imageRepository.findById(id).get();
        
        return ResponseEntity.ok()
               .contentType(MediaType.parseMediaType(profileImage.getFileType()))
               .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename = " + profileImage.getName())
               .body(new ByteArrayResource(profileImage.getFileData()));
    
    }
}
