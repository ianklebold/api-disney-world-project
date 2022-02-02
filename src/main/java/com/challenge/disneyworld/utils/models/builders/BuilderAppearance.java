package com.challenge.disneyworld.utils.models.builders;

import java.time.LocalDate;

import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.utils.models.ModelDetailAppearance;
import com.challenge.disneyworld.utils.models.ModelImage;
import com.challenge.disneyworld.utils.models.ModelListAppearance;

public class BuilderAppearance implements IAppearanceBuilder{
    
    private String title;
    private ModelImage image;
    private LocalDate creation_date;

    public BuilderAppearance setTitle(String title){
        this.title = title;
        return this;
    }

    public BuilderAppearance setCreation_Date(LocalDate localDate){
        this.creation_date = localDate;
        return this;
    }

    public BuilderAppearance setImage(ProfileImage image){
        if(image != null){
            String url = "http://localhost:8080/disneyworld/api/v1/avatar/";
            this.image = new ModelImage(image.getName(), url+image.getId());
        }
        return this;
    }


    @Override
    public ModelListAppearance modelListAppearance(){
        ModelListAppearance modelListAppearance = new ModelListAppearance();
        modelListAppearance.setTitle(this.title);
        modelListAppearance.setCreation_date(this.creation_date);
        modelListAppearance.setImage(this.image);
        return modelListAppearance;
    }

    @Override
    public ModelDetailAppearance modelDetailAppearance() {
        // TODO Auto-generated method stub
        return null;
    }

}
