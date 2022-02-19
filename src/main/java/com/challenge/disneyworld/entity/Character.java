package com.challenge.disneyworld.entity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

@Entity
public class Character {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "born_date", nullable = false, updatable = false)
    private LocalDate born_date;

    @NotEmpty
    @Column(name = "name", nullable = false, updatable = true, unique = true)
    private String name;

    @NotEmpty
    @Column(name = "history", nullable = false, updatable= true)
    private String history;

    /*Relationship*/
    @ManyToMany(mappedBy = "characters")
    private List<Film> appearances = new ArrayList<Film>();

    @OneToOne
    @JoinColumn(name="profile_image")
    private ProfileImage profileimage;

    @OneToMany(cascade={CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    @JoinColumn(name="image_character")
    private List<PostImage> postImage = new ArrayList<PostImage>();


    public Character() {}

    public Character(@NotEmpty LocalDate born_date, @NotEmpty String name, @NotEmpty String history) {
        this.born_date = born_date;
        this.name = name;
        this.history = history;
    }

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
    public LocalDate getBorn_date() {
        return born_date;
    }

    /**
     * @param born_date the born_date to set
     */
    public void setBorn_date(LocalDate born_date) {
        this.born_date = born_date;
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
        this.name = name.trim();
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
        this.history = history.trim();
    }

    /**
     * @return List<Appearance> return the appearances
     */
    public List<Film> getAppearances() {
        return appearances;
    }

    /**
     * @param appearances the appearances to set
     */
    public void setAppearances(List<Film> appearances) {
        this.appearances = appearances;
        for (Film appearance : appearances) {
            appearance.getCharacters().add(this);
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

    public void addImages(ArrayList<PostImage> postImage) {
        for (PostImage element : postImage) {
            this.getPostImage().add(element);
        }
    }

}
