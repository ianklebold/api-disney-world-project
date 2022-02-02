package com.challenge.disneyworld.repository;

import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Appearance;
import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppearanceRepository extends CrudRepository<Appearance,Long>{
    Optional<Appearance> findByTitle(String title);
    
    ArrayList<Appearance> findByTitleIgnoreCaseContaining(String title);
    ArrayList<Appearance> findAllByType(EnumTypeAppearance type);
}
