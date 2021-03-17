package com.lunardi.alfood.infrastructure.repository.spec;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.Specification;

import com.lunardi.alfood.domain.model.Restaurante;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RestauranteSpecs {
	
	public static Specification<Restaurante> comFreteGratis() {
		return (root, query, builder) ->
			builder.equal(root.get("taxaFrete"), BigDecimal.ZERO);
	}
	
	public static Specification<Restaurante> comNomeSemelhante(String nome) {
		return (root, query, builder) -> 
			builder.like(root.get("nome"), "%" + nome  + "%");
	}
	
}
