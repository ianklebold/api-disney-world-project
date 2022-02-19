package com.challenge.disneyworld.dto;

public class ModelListCharacter {
    private String name;
    private ModelImage imageProfile;

    public ModelListCharacter() {}

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
     * @return ModelImage return the imageProfile
     */
    public ModelImage getImageProfile() {
        return imageProfile;
    }

    /**
     * @param imageProfile the imageProfile to set
     */
    public void setImageProfile(ModelImage imageProfile) {
        this.imageProfile = imageProfile;
    }

}
