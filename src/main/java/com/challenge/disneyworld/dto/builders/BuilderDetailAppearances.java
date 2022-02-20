package com.challenge.disneyworld.dto.builders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.challenge.disneyworld.dto.ModelCharacter;
import com.challenge.disneyworld.dto.ModelDetailAppearance;
import com.challenge.disneyworld.dto.ModelImage;
import com.challenge.disneyworld.dto.ModelListAppearance;
import com.challenge.disneyworld.dto.builders.interfaces.IAppearanceBuilder;
import com.challenge.disneyworld.entity.Character;
import com.challenge.disneyworld.entity.Genre;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;
import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;

public class BuilderDetailAppearances implements IAppearanceBuilder{

    private Long id;
    private String title;
    private LocalDate creation_date;
    private int calification;
    private String history;
    private EnumTypeAppearance type;
    private String genre;
    private ModelImage profileImage;
    private ArrayList<ModelImage> postsImages = new ArrayList<ModelImage>();
    private ArrayList<ModelCharacter> listCharacters = new ArrayList<ModelCharacter>();

    public BuilderDetailAppearances setGenre(Genre genre){
        if(genre != null){
            this.genre = genre.getName();
        }
        return this;
    }

    public BuilderDetailAppearances setId(Long id){
        this.id = id;
        return this;
    }

    public BuilderDetailAppearances setProfileImage(ProfileImage image){
        if(image != null){
            String url = "http://localhost:8080/disneyworld/api/v1/avatar/";
            this.profileImage = new ModelImage(image.getName(), url+image.getId());
            
        }
        return this;
    }

    public BuilderDetailAppearances setPostImage(List<PostImage> list){
        String url = "http://localhost:8080/disneyworld/api/v1/postimages/";
        if(list.size() > 0){
            for (PostImage element : list) {
                this.postsImages.add(new ModelImage(element.getName(), 
                                     url+element.getId()));
            }
        }
        return this;
    }

    public BuilderDetailAppearances setListCharacters(List<Character> list){
        String url = "http://localhost:8080/disneyworld/api/v1/characters/"; 
        if(list.size()>0){
            for (Character c : list) {
                this.listCharacters.add(new ModelCharacter(c.getName(),url+c.getId()));
            }
        }
        return this;
    }



    public BuilderDetailAppearances setTitle(String title){
        this.title = title;
        return this;
    }

    public BuilderDetailAppearances setCreationDate(LocalDate creationDate){
        this.creation_date = creationDate;
        return this;
    }

    public BuilderDetailAppearances setCalification(int calification){
        this.calification = calification;
        return this;
    }

    public BuilderDetailAppearances setHistory(String history){
        this.history = history;
        return this;
    }

    public BuilderDetailAppearances setType(EnumTypeAppearance type){
        this.type = type;
        return this;
    }

    @Override
    public ModelDetailAppearance modelDetailAppearance(){
        ModelDetailAppearance appearance = new ModelDetailAppearance();
        appearance.setId(this.id);
        appearance.setTitle(this.title);
        appearance.setCalification(this.calification);
        appearance.setCreation_date(this.creation_date);
        appearance.setHistory(this.history);
        appearance.setNameGenere(this.genre);
        appearance.setType(this.type);
        appearance.setListCharacters(this.listCharacters);
        appearance.setProfileimage(this.profileImage);
        appearance.setPostImage(this.postsImages);
        return appearance;
    }

    @Override
    public ModelListAppearance modelListAppearance() {
        
        return null;
    }

    
    
}
