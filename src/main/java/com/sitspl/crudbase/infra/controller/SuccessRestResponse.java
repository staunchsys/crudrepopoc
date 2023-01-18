package com.sitspl.crudbase.infra.controller;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class SuccessRestResponse<T> {

	private String status;
    private int httpcode;
    private T data;
    
}
