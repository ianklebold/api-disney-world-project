package com.challenge.disneyworld.entity;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity
@PrimaryKeyJoinColumn(name = "id_image")
public class ProfileImage extends Image{
    
}
