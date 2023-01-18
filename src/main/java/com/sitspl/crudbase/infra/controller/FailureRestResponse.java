package com.sitspl.crudbase.infra.controller;

import java.util.HashMap;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@JsonInclude(Include.NON_NULL)
public class FailureRestResponse {

	private String status;
    private int httpcode;
    private String errorMessage;
    private String stackTrace;
    private HashMap<String, String[]> fieldErrors;

}
