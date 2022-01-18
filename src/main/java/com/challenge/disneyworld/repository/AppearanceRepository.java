package com.challenge.disneyworld.repository;

import java.util.Optional;

import com.challenge.disneyworld.entity.Appearance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppearanceRepository extends CrudRepository<Appearance,Long>{
    Optional<Appearance> findByTitle(String title);
}
