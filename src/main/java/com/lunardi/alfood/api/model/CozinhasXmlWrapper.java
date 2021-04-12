package com.lunardi.alfood.api.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.lunardi.alfood.domain.model.Cozinha;

import lombok.Data;
import lombok.NonNull;

//@JacksonXmlRootElement(localName = "cozinhas")
@Data
public class CozinhasXmlWrapper {

	@JsonProperty("cozinha")
	//@JacksonXmlElementWrapper(useWrapping = false)
	@NonNull
	private List<Cozinha> cozinhas;
	
}
