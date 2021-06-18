package com.lunardi.alfood.core.jackson;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.module.SimpleModule;
import com.lunardi.alfood.api.model.mixin.CidadeMixin;
import com.lunardi.alfood.api.model.mixin.CozinhaMixin;
import com.lunardi.alfood.domain.model.Cidade;
import com.lunardi.alfood.domain.model.Cozinha;

@Component
public class JacksonMixinModule extends SimpleModule {

	private static final long serialVersionUID = 1L;

	public JacksonMixinModule() {
		//setMixInAnnotation(Restaurante.class, RestauranteMixin.class);
		setMixInAnnotation(Cidade.class, CidadeMixin.class);
	    setMixInAnnotation(Cozinha.class, CozinhaMixin.class);
	}
	
}
