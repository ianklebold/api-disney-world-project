package com.challenge.disneyworld.utils.models;

import java.time.LocalDate;
import java.util.ArrayList;

import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;

public class ModelDetailAppearance {
    private Long id;
    private String title;
    private LocalDate creation_date;
    private int calification;
    private String history;
    private EnumTypeAppearance type;
    private String nameGenere;
    private ArrayList<ModelCharacter> listCharacters = new ArrayList<ModelCharacter>();
//  private String profileimage;
//  private List<PostImage> postImage = new ArrayList<PostImage>();




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
     * @return int return the calification
     */
    public int getCalification() {
        return calification;
    }

    /**
     * @param calification the calification to set
     */
    public void setCalification(int calification) {
        this.calification = calification;
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
     * @return EnumTypeAppearance return the type
     */
    public EnumTypeAppearance getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(EnumTypeAppearance type) {
        this.type = type;
    }

    /**
     * @return String return the nameGenere
     */
    public String getNameGenere() {
        return nameGenere;
    }

    /**
     * @param nameGenere the nameGenere to set
     */
    public void setNameGenere(String nameGenere) {
        this.nameGenere = nameGenere;
    }


    /**
     * @return ArrayList<ModelCharacter> return the listCharacters
     */
    public ArrayList<ModelCharacter> getListCharacters() {
        return listCharacters;
    }

    /**
     * @param listCharacters the listCharacters to set
     */
    public void setListCharacters(ArrayList<ModelCharacter> listCharacters) {
        this.listCharacters = listCharacters;
    }

}

