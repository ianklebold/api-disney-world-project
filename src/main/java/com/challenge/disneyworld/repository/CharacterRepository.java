package com.challenge.disneyworld.repository;
import com.challenge.disneyworld.entity.Character;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends CrudRepository<Character,Long>{
}
