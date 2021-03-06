package com.challenge.disneyworld.service;

import java.util.ArrayList;

import com.challenge.disneyworld.entity.PostImage;
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
        if(image != null){
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
        }else{
            return null;
        }

    }  

    public ArrayList<PostImage> uploadImagePostToDB(ArrayList<MultipartFile> postImage){
        if(postImage != null && postImage.size()>0){
            ArrayList<PostImage> Images = new ArrayList<PostImage>();
            for (MultipartFile image : postImage) {
                try {
                    Images.add(
                    new PostImage(
                        image.getOriginalFilename(),
                        image.getContentType(),
                        image.getBytes()
                    )
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            for (PostImage post_image : Images) {
                imageRepository.save(post_image);
            }
            return Images;
        }else{
            return null;
        }
    } 

    public void deleteImagePostToDB(ArrayList<PostImage> postImage){
        for (PostImage element : postImage) {
            imageRepository.delete(element);
        }
    }

}
