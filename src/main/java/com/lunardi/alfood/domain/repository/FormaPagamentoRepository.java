package com.lunardi.alfood.domain.repository;

import java.util.List;

import com.lunardi.alfood.domain.model.FormaPagamento;

public interface FormaPagamentoRepository {

	List<FormaPagamento> listar();

	FormaPagamento buscar(Long id);

	FormaPagamento salvar(FormaPagamento formaPagamento);

	void remover(FormaPagamento formaPagamento);

}
