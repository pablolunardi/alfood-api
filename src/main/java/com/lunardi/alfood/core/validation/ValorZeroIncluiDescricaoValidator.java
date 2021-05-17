package com.lunardi.alfood.core.validation;

import java.beans.PropertyDescriptor;
import java.math.BigDecimal;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;

import org.springframework.beans.BeanUtils;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {

	private String valorField;
	private String descricaoField;
	private String descricaoObrigatoria;
	
	@Override
	public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
		this.valorField = constraintAnnotation.valorField();
		this.descricaoField = constraintAnnotation.descricaoField();
		this.descricaoObrigatoria = constraintAnnotation.descricaoObrigatoria();
	}
	
	@Override
	public boolean isValid(Object objetoValidacao, ConstraintValidatorContext context) {
		boolean valido = true;
		
		try {
			PropertyDescriptor propertyDescriptorValor = BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), this.valorField);
			PropertyDescriptor propertyDescriptorDescricao = BeanUtils.getPropertyDescriptor(objetoValidacao.getClass(), this.descricaoField);
			
			if (propertyDescriptorValor != null && propertyDescriptorDescricao != null) {
				BigDecimal valor = (BigDecimal) propertyDescriptorValor.getReadMethod().invoke(objetoValidacao);
				
				String descricao = (String) propertyDescriptorDescricao.getReadMethod().invoke(objetoValidacao);
				
				if (valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
					valido = descricao.toLowerCase().contains(this.descricaoObrigatoria.toLowerCase());
				}
			}

			return valido;
		} catch (Exception e) {
			throw new ValidationException(e);
		}
	}

}
