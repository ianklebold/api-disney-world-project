package com.challenge.disneyworld.repository;

import com.challenge.disneyworld.entity.Genre;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre,Long>{
    
}
