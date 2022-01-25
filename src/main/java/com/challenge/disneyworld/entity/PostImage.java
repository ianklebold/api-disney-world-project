package com.challenge.disneyworld.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class PostImage extends Image{

    public PostImage(){}

    public PostImage(String originalFilename, String contentType, byte[] bytes) {
        this.setName(originalFilename);
        this.setFileType(contentType);
        this.setFileData(bytes);
    }


}
