package com.sitspl.crudbase.infra.exception;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;

import lombok.Getter;
import lombok.Setter;

public class DeserializerException extends JsonMappingException {

	private static final long serialVersionUID = 1L;
	
	private static final String MSG = "Incorrect data passed for this field";
	
	@Getter @Setter
	private String field;
	
	@Getter @Setter
	private String message = MSG;
	
	public DeserializerException(JsonParser p, String field, String message, Throwable throwable) {
		super(p, MSG);
		this.field = field;
		this.message = message;
	}
	
	public DeserializerException(JsonParser p, String field, Throwable throwable) {
		super(p, MSG);
		this.field = field;
		this.message = this.message + " : " + this.field;
	}

	
	
}
