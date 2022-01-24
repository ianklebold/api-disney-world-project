package com.challenge.disneyworld.service;

import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.repository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {
    @Autowired
    ImageRepository imageRepository;

    public ProfileImage uploadImageProfileToDB(MultipartFile image){
        ProfileImage profileImage = new ProfileImage();
        try {
            profileImage.setFileData(image.getBytes());
            profileImage.setFileType(image.getContentType());
            profileImage.setName(image.getOriginalFilename());
            imageRepository.save(profileImage);
            return profileImage;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }


    }   
}
