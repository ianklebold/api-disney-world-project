package com.challenge.disneyworld.utils.models;

import java.util.ArrayList;
import java.util.List;

public class ModelDetailGenre {
    private String nameGenre;
    private ModelImage image;
    private List<ModelAppearance> appearances = new ArrayList<ModelAppearance>(); 
    
    public ModelDetailGenre() {}
    
    /**
     * @return String return the nameGenre
     */
    public String getNameGenre() {
        return nameGenre;
    }

    /**
     * @param nameGenre the nameGenre to set
     */
    public void setNameGenre(String nameGenre) {
        this.nameGenre = nameGenre;
    }

    /**
     * @return ModelImage return the image
     */
    public ModelImage getImage() {
        return image;
    }

    /**
     * @param image the image to set
     */
    public void setImage(ModelImage image) {
        this.image = image;
    }

    /**
     * @return List<ModelAppearance> return the appearances
     */
    public List<ModelAppearance> getAppearances() {
        return appearances;
    }

    /**
     * @param appearances the appearances to set
     */
    public void setAppearances(List<ModelAppearance> appearances) {
        this.appearances = appearances;
    }

}
