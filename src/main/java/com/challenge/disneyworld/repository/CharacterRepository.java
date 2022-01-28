package com.challenge.disneyworld.repository;
import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Character;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends CrudRepository<Character,Long>{
    Optional<Character> findByName(String name);
    ArrayList<Character> findByNameIgnoreCaseContaining(String name);
}
