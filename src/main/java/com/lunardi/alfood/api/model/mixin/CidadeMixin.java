package com.lunardi.alfood.api.model.mixin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lunardi.alfood.domain.model.Estado;

public abstract class CidadeMixin {

    @JsonIgnoreProperties(value = "nome", allowGetters = true)
    private Estado estado;
    
}     

