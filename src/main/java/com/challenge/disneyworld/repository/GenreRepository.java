package com.challenge.disneyworld.repository;

import java.util.Optional;

import com.challenge.disneyworld.entity.Genre;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository extends CrudRepository<Genre,Long>{
    Optional<Genre> findByName(String name);
}
