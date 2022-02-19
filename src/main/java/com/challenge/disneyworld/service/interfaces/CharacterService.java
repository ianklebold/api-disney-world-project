package com.challenge.disneyworld.service.interfaces;

import java.util.ArrayList;

import com.challenge.disneyworld.entity.Character;
import com.challenge.disneyworld.entity.PostImage;
import com.challenge.disneyworld.entity.ProfileImage;

import org.springframework.http.ResponseEntity;

public interface CharacterService {
   
    public ResponseEntity<?> createCharacter(
    ArrayList<PostImage> postImage,
    ProfileImage imageCharacter,
    Character character);

    public ResponseEntity<?>  updateCharacter(
    ArrayList<PostImage> postImage,
    ProfileImage imageCharacter,
    Character character);
    
    public ResponseEntity<?> deleteCharacter(Long id);
    public ResponseEntity<?> getCharacterByName(String name);
    public ResponseEntity<?> getCharacterByAge(int age);
    public ResponseEntity<?> getCharactersByAppearance(Long idAppearance);
    public ResponseEntity<?> getCharacterById(Long id);
    public ResponseEntity<?> getCharacter();
}
