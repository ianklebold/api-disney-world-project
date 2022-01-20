package com.challenge.disneyworld.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Appearance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "title", nullable = false, updatable = true, unique = true)
    private String title;

    @NotEmpty
    @Column(name = "creation_date", nullable = false, updatable = true)
    private LocalDate creation_date;

    @NotEmpty
    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "calification", nullable = false, updatable = true)
    private int calification;
    
    @NotEmpty
    @Column(name = "history", nullable = false, updatable = true)
    private String history;

    @NotEmpty
    @Column(name = "type", nullable = false, updatable = true)
    @Enumerated(value = EnumType.STRING)
    private EnumTypeAppearance type;

    /**Relationship */
    @JsonBackReference
    @ManyToOne(cascade = {})
    private Genre genre;

    @OneToOne
    @JoinColumn(name="profile_image")
    private ProfileImage profileimage;

    @ManyToMany
    @JoinTable(
    name = "character_whit_appearance", 
    joinColumns = @JoinColumn(name = "appearance_id"), 
    inverseJoinColumns = @JoinColumn(name = "character_id"))
    @Column(name = "appearance", updatable = true, nullable = true)
    private List<Character> characters = new ArrayList<Character>();

    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    @JoinColumn(name="image_post")
    private List<PostImage> postImage = new ArrayList<PostImage>();

    /*Constructors*/

    public Appearance(Long id, @NotEmpty String title, @NotEmpty LocalDate creation_date,
            @NotEmpty @Min(1) @Max(5) int calification, @NotEmpty String history,
            @NotEmpty EnumTypeAppearance type, Genre genre) {
        this.id = id;
        this.title = title;
        this.creation_date = creation_date;
        this.calification = calification;
        this.history = history;
        this.type = type;
        this.genre = genre;
    }

    public Appearance() {}



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
     * @return Genre return the genre
     */
    public Genre getGenre() {
        return genre;
    }

    /**
     * @param genre the genre to set
     */
    public void setGenre(Genre genre) {
        this.genre = genre;
        if(genre != null){
            genre.getAppearances().add(this);
        }
    }

    /**
     * @return ProfileImage return the profileimage
     */
    public ProfileImage getProfileimage() {
        return profileimage;
    }

    /**
     * @param profileimage the profileimage to set
     */
    public void setProfileimage(ProfileImage profileimage) {
        this.profileimage = profileimage;
    }

    /**
     * @return List<Character> return the characters
     */
    public List<Character> getCharacters() {
        return characters;
    }

    /**
     * @param characters the characters to set
     */
    public void setCharacters(List<Character> characters) {
        this.characters = characters;
        if(characters != null){
            for (Character character : characters) {
                if(character.getAppearances().contains(this)){
                    //Evito añadir dos veces el mismo objeto cuando actualizamos.
                    character.getAppearances().remove(this);
                }
                character.getAppearances().add(this);
            }
        }
    }

    /**
     * @return List<PostImage> return the postImage
     */
    public List<PostImage> getPostImage() {
        return postImage;
    }

    /**
     * @param postImage the postImage to set
     */
    public void setPostImage(List<PostImage> postImage) {
        this.postImage = postImage;
    }

}
