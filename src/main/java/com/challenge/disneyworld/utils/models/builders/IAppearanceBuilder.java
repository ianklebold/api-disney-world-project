package com.challenge.disneyworld.utils.models.builders;

import com.challenge.disneyworld.utils.models.ModelDetailAppearance;
import com.challenge.disneyworld.utils.models.ModelListAppearance;

public interface IAppearanceBuilder{
    ModelListAppearance modelListAppearance();
    ModelDetailAppearance modelDetailAppearance();
}
