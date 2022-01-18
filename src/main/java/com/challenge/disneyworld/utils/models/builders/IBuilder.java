package com.challenge.disneyworld.utils.models.builders;

import com.challenge.disneyworld.utils.models.ModelDetailCharacter;
import com.challenge.disneyworld.utils.models.ModelListCharacter;

public interface IBuilder {
    ModelDetailCharacter builder();
    ModelListCharacter builderListCharacter();
}
