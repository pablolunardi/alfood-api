package com.lunardi.alfood.api.assembler;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lunardi.alfood.api.model.input.RestauranteInput;
import com.lunardi.alfood.domain.model.Cozinha;
import com.lunardi.alfood.domain.model.Restaurante;

@Component
public class RestauranteInputDisassembler {

	@Autowired
	private ModelMapper modelMapper;
	
    public Restaurante toDomainObject(RestauranteInput restauranteInput) {
        return modelMapper.map(restauranteInput, Restaurante.class);
    }
    
    public void copyToDomainObject(RestauranteInput restauranteInput, Restaurante restaurante) {
    	restaurante.setCozinha(new Cozinha());
    	
    	modelMapper.map(restauranteInput, restaurante);
    }
    
}
