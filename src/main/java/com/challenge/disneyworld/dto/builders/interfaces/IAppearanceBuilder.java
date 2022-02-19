package com.challenge.disneyworld.dto.builders.interfaces;

import com.challenge.disneyworld.dto.ModelDetailAppearance;
import com.challenge.disneyworld.dto.ModelListAppearance;

public interface IAppearanceBuilder{
    ModelListAppearance modelListAppearance();
    ModelDetailAppearance modelDetailAppearance();
}
