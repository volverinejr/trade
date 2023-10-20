package br.com.claudemirojr.trade.controller.handler;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLTimeoutException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.claudemirojr.trade.exception.ExceptionResponse;
import br.com.claudemirojr.trade.exception.ResourceNotFoundException;
import br.com.claudemirojr.trade.exception.ResourceServiceValidationException;


@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;	
	
	
	@ExceptionHandler(ResourceNotFoundException.class)
	public final ResponseEntity<ExceptionResponse> handleBadRequestExceptions(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), ex.getMessage(),
				request.getDescription(false));
		return new ResponseEntity<>(exceptionResponse, HttpStatus.NOT_FOUND);
	}	
	
	
	// Campos não passaram em @Valid
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			 HttpHeaders headers,
			 HttpStatusCode status,
			 WebRequest request) {
		ArrayList<ExceptionResponse> erros = new ArrayList<ExceptionResponse>();
		
		ex.getBindingResult().getAllErrors().forEach(error -> {
			String campo = ((FieldError) error).getField();
			String mensagem = messageSource.getMessage(error, LocaleContextHolder.getLocale());

			erros.add(new ExceptionResponse(LocalDateTime.now(), campo, mensagem));
		});

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);		
    }	
	
	
	
	// Validação do dado para a camada de negócio
	@ExceptionHandler(ResourceServiceValidationException.class)
	public final ResponseEntity<Object> handleResourceServiceValidationException(Exception ex, WebRequest request) {
		ArrayList<ExceptionResponse> erros = new ArrayList<ExceptionResponse>();

		String[] campos = ex.getMessage().split(";");
		
		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), campos[0], campos[1]);

		erros.add(exceptionResponse);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}	
	
	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public final ResponseEntity<Object> handleSQLIntegrityConstraintViolationException(Exception ex, WebRequest request) {
		ArrayList<ExceptionResponse> erros = new ArrayList<ExceptionResponse>();

		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), "Violação de integridade", ex.getCause().getCause().getMessage() );

		erros.add(exceptionResponse);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erros);
	}
	
	
	@ExceptionHandler(SQLTimeoutException.class) // InvalidTimeoutException.class
	public final ResponseEntity<Object> handleSQLTimeoutException(Exception ex, WebRequest request) {
		ArrayList<ExceptionResponse> erros = new ArrayList<ExceptionResponse>();

		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), "Consulta excedeu o tempo limite",
				ex.getCause().getCause().getMessage());

		erros.add(exceptionResponse);

		return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body(erros);
	}

	@ExceptionHandler(SQLException .class) // SQLServerException.class
	public final ResponseEntity<Object> handleSQLServerException(Exception ex, WebRequest request) {
		ArrayList<ExceptionResponse> erros = new ArrayList<ExceptionResponse>();

		ExceptionResponse exceptionResponse = new ExceptionResponse(LocalDateTime.now(), "Erro de SQL",
				ex.getCause().getCause().getMessage());

		erros.add(exceptionResponse);

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erros);
	}

	
	

}
