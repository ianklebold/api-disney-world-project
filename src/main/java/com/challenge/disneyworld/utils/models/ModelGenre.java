package com.challenge.disneyworld.utils.models;


public class ModelGenre {
    
    private String name;
    private String urlImage;

    
    public ModelGenre(String name, String urlImage) {
        this.name = name;
        this.urlImage = urlImage;
    }

    /**
     * @return String return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String return the urlImage
     */
    public String getUrlImage() {
        return urlImage;
    }

    /**
     * @param urlImage the urlImage to set
     */
    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

}
