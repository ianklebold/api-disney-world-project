package com.challenge.disneyworld.dto.builders;

import java.util.ArrayList;
import java.util.List;

import com.challenge.disneyworld.dto.ModelAppearance;
import com.challenge.disneyworld.dto.ModelDetailCharacter;
import com.challenge.disneyworld.dto.ModelImage;
import com.challenge.disneyworld.dto.ModelListCharacter;
import com.challenge.disneyworld.dto.builders.interfaces.IBuilder;
import com.challenge.disneyworld.entity.Film;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;

public class BuilderCharacter implements IBuilder{
    private Long id;
    private int age;
    private String name;
    private String history;
    private List<ModelAppearance> appearances = new ArrayList<ModelAppearance>();
    private ModelImage imageProfile;
    private List<ModelImage> imagesPosts = new ArrayList<ModelImage>();

    public BuilderCharacter setId(Long id){
        this.id = id;
        return this;
    }

    public BuilderCharacter setAge(int age){
        this.age = age;
        return this;
    }

    public BuilderCharacter setName(String name){
        this.name = name;
        return this;
    }

    public BuilderCharacter setHistory(String history){
        this.history = history;
        return this;
    }
    
    public BuilderCharacter setAppearances(List<Film> list){
        String url = "http://localhost:8080/disneyworld/api/v1/appearance/"; 

        if(list.size() == 0){
            this.appearances = null;
        }else{
            for (Film appearance : list) {
                this.appearances.add(
                    new ModelAppearance(appearance.getTitle(), url+appearance.getId())
                                );
            }
        }

        return this;
    }

    public BuilderCharacter setProfileImage(ProfileImage image){
        if(image != null){
            String url = "http://localhost:8080/disneyworld/api/v1/avatar/";
            this.imageProfile = new ModelImage(image.getName(), url+image.getId());
        }
        return this;
    }

    public BuilderCharacter setPostImage(List<PostImage> list){
        String url = "http://localhost:8080/disneyworld/api/v1/postimages/";
        if(list.size() > 0){
            for (PostImage element : list) {
                this.imagesPosts.add(new ModelImage(element.getName(), 
                                     url+element.getId()));
            }
        }
        return this;
    }

    @Override
    public ModelDetailCharacter builder(){
        ModelDetailCharacter character = new ModelDetailCharacter();
        character.setId(this.id);
        character.setAge(this.age);
        character.setName(this.name);
        character.setHistory(this.history);
        character.setAppearances(this.appearances);
        character.setImageProfile(this.imageProfile);
        character.setImagesPosts(this.imagesPosts);

        return character;
    }

    @Override
    public ModelListCharacter builderListCharacter(){
        ModelListCharacter character = new ModelListCharacter();
        character.setName(this.name);
        character.setImageProfile(this.imageProfile);
        return character;
    }


}

