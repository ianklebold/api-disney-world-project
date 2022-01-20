package com.challenge.disneyworld.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
public class Genre {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    @Column(name = "name", nullable = false, updatable = true, unique = true)
    private String name;

    /**Relationship */

    @OneToOne
    @JoinColumn(name="profile_image")
    private ProfileImage profileimage;

    @JsonManagedReference
    @OneToMany(mappedBy = "genre",cascade = {CascadeType.PERSIST, CascadeType.REMOVE, CascadeType.REFRESH})
    private List<Appearance> appearances = new ArrayList<Appearance>();
   

    public Genre() {
    }

    public Genre(@NotEmpty String name, List<Appearance> genre) {
        this.name = name;
        this.appearances = genre;
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
     * @return List<Appearance> return the appearances
     */
    public List<Appearance> getAppearances() {
        return appearances;
    }

    /**
     * @param appearances the appearances to set
     */
    public void setAppearances(List<Appearance> appearances) {
        this.appearances = appearances;
    }

}
