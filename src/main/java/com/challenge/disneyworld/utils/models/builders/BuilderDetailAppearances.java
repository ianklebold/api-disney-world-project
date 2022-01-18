package com.challenge.disneyworld.utils.models.builders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.challenge.disneyworld.entity.Character;
import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;
import com.challenge.disneyworld.utils.models.ModelCharacter;
import com.challenge.disneyworld.utils.models.ModelDetailAppearance;
import com.challenge.disneyworld.utils.models.ModelListAppearance;

public class BuilderDetailAppearances implements IAppearanceBuilder{

    private Long id;
    private String title;
    private LocalDate creation_date;
    private int calification;
    private String history;
    private EnumTypeAppearance type;
    private String nameGenere;
    private ArrayList<ModelCharacter> listCharacters = new ArrayList<ModelCharacter>();

    public BuilderDetailAppearances setId(Long id){
        this.id = id;
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

    public BuilderDetailAppearances setGenere(String name){
        this.nameGenere = name;
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
        appearance.setNameGenere(this.nameGenere);
        appearance.setType(this.type);
        appearance.setListCharacters(this.listCharacters);
        return appearance;
    }

    @Override
    public ModelListAppearance modelListAppearance() {
        
        return null;
    }

    
    
}
