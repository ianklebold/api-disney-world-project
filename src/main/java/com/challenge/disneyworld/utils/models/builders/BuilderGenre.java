package com.challenge.disneyworld.utils.models.builders;

import java.util.ArrayList;
import java.util.List;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.utils.models.ModelAppearance;
import com.challenge.disneyworld.utils.models.ModelDetailGenre;
import com.challenge.disneyworld.utils.models.ModelImage;

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

    public BuilderGenre setAppearances(List<Appearance> newAppearances){
        String url = "http://localhost:8080/disneyworld/api/v1/appearance/";
        if(newAppearances.size() == 0){
            this.appearances = null;
        }else{
            for (Appearance appearance : newAppearances) {
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
