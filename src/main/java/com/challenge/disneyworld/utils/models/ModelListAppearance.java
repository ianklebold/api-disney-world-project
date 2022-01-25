package com.challenge.disneyworld.utils.models;

import java.time.LocalDate;

public class ModelListAppearance {
    
    private String title;
    private ModelImage image;
    private LocalDate creation_date;

    public ModelListAppearance() {}

    /**
     * @return String return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return LocalDate return the creation_date
     */
    public LocalDate getCreation_date() {
        return creation_date;
    }

    /**
     * @param creation_date the creation_date to set
     */
    public void setCreation_date(LocalDate creation_date) {
        this.creation_date = creation_date;
    }

    /**
     * @return ProfileImage return the image
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

}
