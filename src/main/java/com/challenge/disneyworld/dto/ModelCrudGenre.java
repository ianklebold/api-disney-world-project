package com.challenge.disneyworld.dto;

public class ModelCrudGenre {
    private String name;

    public ModelCrudGenre() {
    }

    public ModelCrudGenre(String name) {
        this.name = name;
        
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

}
