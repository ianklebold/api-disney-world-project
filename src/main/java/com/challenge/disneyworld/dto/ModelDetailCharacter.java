package com.challenge.disneyworld.dto;

import java.util.ArrayList;
import java.util.List;

public class ModelDetailCharacter {
    private Long id;
    private int age;
    private String name;
    private String history;
    private Double weight;
    private List<ModelAppearance> appearances = new ArrayList<ModelAppearance>();
    private ModelImage imageProfile;
    private List<ModelImage> imagesPosts = new ArrayList<ModelImage>();

    public ModelDetailCharacter() {}

    /**
     * @return Long return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return LocalDate return the born_date
     */
    public int getAge() {
        return age;
    }

    /**
     * @param born_date the born_date to set
     */
    public void setAge(int age) {
        this.age = age;
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
     * @return String return the history
     */
    public String getHistory() {
        return history;
    }

    /**
     * @param history the history to set
     */
    public void setHistory(String history) {
        this.history = history;
    }

    /**
     * @return List<Set> return the appearances
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

    /**
     * @return List<ModelImage> return the imagesPosts
     */
    public List<ModelImage> getImagesPosts() {
        return imagesPosts;
    }

    /**
     * @param imagesPosts the imagesPosts to set
     */
    public void setImagesPosts(List<ModelImage> imagesPosts) {
        this.imagesPosts = imagesPosts;
    }


    /**
     * @return Double return the weight
     */
    public Double getWeight() {
        return weight;
    }

    /**
     * @param weight the weight to set
     */
    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
