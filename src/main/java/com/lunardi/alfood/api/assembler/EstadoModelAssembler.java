package com.lunardi.alfood.api.assembler;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.lunardi.alfood.api.model.EstadoModel;
import com.lunardi.alfood.domain.model.Estado;

@Component
public class EstadoModelAssembler {

    @Autowired
    private ModelMapper modelMapper;
    
    public EstadoModel toModel(Estado estado) {
        return modelMapper.map(estado, EstadoModel.class);
    }
    
    public List<EstadoModel> toCollectionModel(List<Estado> estados) {
        return estados.stream()
                .map(this::toModel)
                .collect(Collectors.toList());
    }
    
}
