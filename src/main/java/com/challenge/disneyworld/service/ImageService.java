package com.challenge.disneyworld.service;

import com.challenge.disneyworld.entity.Image;
import com.challenge.disneyworld.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ImageService {
    @Autowired
    ImageRepository imageRepository;

    public ResponseEntity<Resource> generateImage(Image image){
        

        return ResponseEntity.ok()
               .contentType(MediaType.parseMediaType(image.getFileType()))
               .header(HttpHeaders.CONTENT_DISPOSITION,"attachment; filename = " + image.getName())
               .body(new ByteArrayResource(image.getFileData()));
    }
}
