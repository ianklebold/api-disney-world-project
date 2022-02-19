package com.challenge.disneyworld.dto.builders;

import java.util.ArrayList;
import java.util.List;

import com.challenge.disneyworld.dto.ModelAppearance;
import com.challenge.disneyworld.dto.ModelDetailGenre;
import com.challenge.disneyworld.dto.ModelImage;
import com.challenge.disneyworld.dto.builders.interfaces.IGenreBuilder;
import com.challenge.disneyworld.entity.Film;
import com.challenge.disneyworld.entity.ProfileImage;

public class BuilderGenre implements IGenreBuilder{
    private String nameGenre;
    private ModelImage image;
    private List<ModelAppearance> appearances = new ArrayList<ModelAppearance>(); 

    public BuilderGenre setNameGenre(String nameGenre){
        this.nameGenre = nameGenre;
        return this;
    }

    public BuilderGenre setImage(ProfileImage image){
        if(image != null){
            String url = "http://localhost:8080/disneyworld/api/v1/avatar/";
            this.image = new ModelImage(image.getName(), url+image.getId());
        }
        return this;
    }

    public BuilderGenre setAppearances(List<Film> newAppearances){
        String url = "http://localhost:8080/disneyworld/api/v1/appearance/";
        if(newAppearances.size() == 0){
            this.appearances = null;
        }else{
            for (Film appearance : newAppearances) {
                this.appearances.add(
                    new ModelAppearance(appearance.getTitle(), url+appearance.getId())
                );
            }
        }
        return this;
    }

    @Override
    public ModelDetailGenre builder() {
        ModelDetailGenre modelDetailGenre = new ModelDetailGenre();
        modelDetailGenre.setNameGenre(this.nameGenre);
        modelDetailGenre.setImage(this.image);
        modelDetailGenre.setAppearances(this.appearances);
        return modelDetailGenre;
    }
    
}
