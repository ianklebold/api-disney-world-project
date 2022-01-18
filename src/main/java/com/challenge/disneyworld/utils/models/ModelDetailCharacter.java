package com.challenge.disneyworld.utils.models;

import java.util.ArrayList;
import java.util.List;

public class ModelDetailCharacter {
    private Long id;
    private int age;
    private String name;
    private String history;
    private List<ModelAppearance> appearances = new ArrayList<ModelAppearance>();
   // private String imageProfile;
   // private List<String> imagesPosts = new ArrayList<String>();

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
     * @return String return the imageProfile
     */
 /*   public String getImageProfile() {
        return imageProfile;
    }
*/
    /**
     * @param imageProfile the imageProfile to set
     */
 /*   public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }
*/
    /**
     * @return List<String> return the imagesPosts
     */
/*    public List<String> getImagesPosts() {
        return imagesPosts;
    }
*/
    /**
     * @param imagesPosts the imagesPosts to set
     */
/*    public void setImagesPosts(List<String> imagesPosts) {
        this.imagesPosts = imagesPosts;
    }
*/
}
