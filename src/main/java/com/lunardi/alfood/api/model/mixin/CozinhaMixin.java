package com.lunardi.alfood.api.model.mixin;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lunardi.alfood.domain.model.Restaurante;

public abstract class CozinhaMixin {

    @JsonIgnore
    private List<Restaurante> restaurantes;
   
}