package com.challenge.disneyworld.repository;

import java.util.ArrayList;
import java.util.Optional;

import com.challenge.disneyworld.entity.Film;
import com.challenge.disneyworld.utils.enumerations.EnumTypeAppearance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmRepository extends CrudRepository<Film,Long>{
    Optional<Film> findByTitle(String title);
    
    ArrayList<Film> findByTitleIgnoreCaseContaining(String title);
    ArrayList<Film> findAllByType(EnumTypeAppearance type);
    ArrayList<Film> findAllByOrderByCreationAsc();
    ArrayList<Film> findAllByOrderByCreationDesc();
}
