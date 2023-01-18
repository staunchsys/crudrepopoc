package com.sitspl.crudbase.infra.exception;

public class SearchFilterException extends Exception {

	private static final long serialVersionUID = 1986965153171685050L;

	private static final String MSG = "Invalid filter option passed";
	
	public SearchFilterException() {
		super(MSG);
	}
}
