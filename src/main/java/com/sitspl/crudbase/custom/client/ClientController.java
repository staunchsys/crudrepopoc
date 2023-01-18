package com.sitspl.crudbase.custom.client;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sitspl.crudbase.infra.controller.CrudController;
import com.sitspl.crudbase.infra.exception.SearchFilterException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(path =  "/api/client")
public class ClientController extends CrudController<Client, ClientService> {
	public ClientController(ClientService studentService) {
		super(Client.class, studentService);
	}
	
	@GetMapping(value = "/listByFilter")
	@ApiImplicitParams({
		@ApiImplicitParam(name = CURRENT_PAGE_KEY, paramType = "query", dataTypeClass =  Integer.class, value = "Current Page to retrieve"),
		@ApiImplicitParam(name = RESULTS_PER_PAGE_KEY, paramType = "query", dataTypeClass =  Integer.class, value = "Results Per Page to retrieve"),
		/*
		@ApiImplicitParam(name = "name", paramType = "query", dataTypeClass =  String.class, value = "Filter by name"),
		@ApiImplicitParam(name = "phone", paramType = "query", dataTypeClass =  String.class, value = "Filter by phone"),
		@ApiImplicitParam(name = "email", paramType = "query", dataTypeClass =  String.class, value = "Filter by email"),
		@ApiImplicitParam(name = "company", paramType = "query", dataTypeClass =  String.class, value = "Filter by company"),
		*/
		@ApiImplicitParam(name = "deletedOn", paramType = "query", dataTypeClass =  String.class, value = "Filter by deletedOn")
	})
	public ResponseEntity<Page<Client>> getByFilter(@ApiParam(hidden = true) @RequestParam(required = false) MultiValueMap<String, String> filters) throws SearchFilterException {
		return super.getByFilter(filters);
	}
}
