package com.challenge.disneyworld.utils.models.builders;

import com.challenge.disneyworld.utils.models.ModelCharacter;
import com.challenge.disneyworld.utils.models.ModelListCharacter;

public interface IBuilder {
    ModelCharacter builder();
    ModelListCharacter builderListCharacter();
}
