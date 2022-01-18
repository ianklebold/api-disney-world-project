package com.challenge.disneyworld.utils.models.builders;

import java.util.ArrayList;
import java.util.List;


import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.utils.models.ModelAppearance;
import com.challenge.disneyworld.utils.models.ModelDetailCharacter;
import com.challenge.disneyworld.utils.models.ModelListCharacter;

public class BuilderCharacter implements IBuilder{
    private Long id;
    private int age;
    private String name;
    private String history;
    private List<ModelAppearance> appearances = new ArrayList<ModelAppearance>();

    public BuilderCharacter setId(Long id){
        this.id = id;
        return this;
    }

    public BuilderCharacter setAge(int age){
        this.age = age;
        return this;
    }

    public BuilderCharacter setName(String name){
        this.name = name;
        return this;
    }

    public BuilderCharacter setHistory(String history){
        this.history = history;
        return this;
    }
    
    public BuilderCharacter setAppearances(List<Appearance> list){
        String url = "http://localhost:8080/disneyworld/api/v1/appearance/"; 

        if(list.size() == 0){
            this.appearances = null;
        }else{
            for (Appearance appearance : list) {
                this.appearances.add(
                    new ModelAppearance(appearance.getTitle(), url+appearance.getId())
                                );
            }
        }

        return this;
    }

    @Override
    public ModelDetailCharacter builder(){
        ModelDetailCharacter character = new ModelDetailCharacter();
        character.setId(this.id);
        character.setAge(this.age);
        character.setName(this.name);
        character.setHistory(this.history);
        character.setAppearances(this.appearances);
        //Falta la imagen e imagenes

        return character;
    }

    @Override
    public ModelListCharacter builderListCharacter(){
        ModelListCharacter character = new ModelListCharacter();
        character.setName(this.name);
        //Falta la imagen de perfil

        return character;
    }


}

