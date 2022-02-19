package com.challenge.disneyworld.dto.builders.interfaces;

import com.challenge.disneyworld.dto.ModelDetailCharacter;
import com.challenge.disneyworld.dto.ModelListCharacter;

public interface IBuilder {
    ModelDetailCharacter builder();
    ModelListCharacter builderListCharacter();
}
