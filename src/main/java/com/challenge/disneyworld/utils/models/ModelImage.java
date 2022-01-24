package com.challenge.disneyworld.utils.models;

public class ModelImage {
    private String imageName;
    private String urlImage;

    public ModelImage(String imageName, String urlImage){
        this.imageName = imageName;
        this.urlImage = urlImage;
    }

    /**
     * @return String return the imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * @param imageName the imageName to set
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
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
