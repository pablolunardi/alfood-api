package com.lunardi.alfood.api.exceptionhandler;

import java.time.LocalDateTime;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.lunardi.alfood.domain.exception.EntidadeEmUsoException;
import com.lunardi.alfood.domain.exception.EntidadeNaoEncontradaException;
import com.lunardi.alfood.domain.exception.NegocioException;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(EntidadeNaoEncontradaException.class)
	public ResponseEntity<Object> tratarEntidadeNaoEncontradoException(EntidadeNaoEncontradaException ex,
			WebRequest request) {
		Problema problema = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem(ex.getMessage()).build();
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler(NegocioException.class)
	public ResponseEntity<Object> tratarNegocioException(NegocioException ex, WebRequest request) {
		Problema problema = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem(ex.getMessage()).build();
		
		return handleExceptionInternal(ex, problema, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	@ExceptionHandler(EntidadeEmUsoException.class)
	public ResponseEntity<Object> tratarEntidadeEmUsoException(EntidadeEmUsoException ex, WebRequest request) {
		Problema problema = Problema.builder()
				.dataHora(LocalDateTime.now())
				.mensagem(ex.getMessage()).build();
	
		return handleExceptionInternal(ex, problema, new HttpHeaders(), HttpStatus.CONFLICT, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = Problema.builder().dataHora(LocalDateTime.now())
					.mensagem(status.getReasonPhrase()).build();
		} else if (body instanceof String) {
			body = Problema.builder().dataHora(LocalDateTime.now())
					.mensagem((String) body);
		}
		
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}
	
}
