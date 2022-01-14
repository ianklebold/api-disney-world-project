package com.challenge.disneyworld.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id_image")
public class PostImage extends Image{

    @Column(name = "image_post")
    private Long characterId;

}
