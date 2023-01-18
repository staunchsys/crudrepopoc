package com.sitspl.crudbase.infra.controller;

import java.util.HashMap;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.sitspl.crudbase.infra.exception.DeserializerException;

import lombok.extern.log4j.Log4j2;

@ControllerAdvice
@Log4j2
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {

		// Condition to not change response for the required package or method name.
		// 1. Swagger APIs.
		// 2. Exceptions handling.
		if (returnType != null && returnType.getMethod() != null && (returnType.getMethod().getDeclaringClass()
				.getPackageName().equals("springfox.documentation.swagger.web")
				|| returnType.getMethod().getDeclaringClass().getPackageName()
						.equals("springfox.documentation.swagger2.web")
				|| ((returnType.getMethod().getName().equals("handleControllerException") 
						|| returnType.getMethod().getName().equals("handleValidationExceptions")
						) && returnType.getMethod()
						.getDeclaringClass().getPackageName().equals("com.sitspl.crudbase.infra.controller")))) {
			return false;
		}

		return true;
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
			ServerHttpResponse response) {

		final SuccessRestResponse<Object> responseObj = new SuccessRestResponse<>();
		responseObj.setData(body);
		responseObj.setHttpcode(200);
		responseObj.setStatus("Success");

		return responseObj;
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseBody
	public ResponseEntity<FailureRestResponse> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
		
		FailureRestResponse failureRestResponse = new FailureRestResponse();
		failureRestResponse.setStatus("Fail");
	    failureRestResponse.setHttpcode(HttpStatus.BAD_REQUEST.value());
	    failureRestResponse.setErrorMessage("Invalid fields passed. Please resolve the same.");
	    
	    HashMap<String, String[]> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String[] errorMessage = new String[] {error.getDefaultMessage()};
	        errors.put(fieldName, errorMessage);
	    });
	    
	    failureRestResponse.setFieldErrors(errors);
	    
	    return ResponseEntity.of(Optional.of(failureRestResponse));
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseBody
	public ResponseEntity<FailureRestResponse> handleValidationExceptions(
			HttpMessageNotReadableException ex) {
		
		FailureRestResponse failureRestResponse = new FailureRestResponse();
		failureRestResponse.setStatus("Fail");
	    failureRestResponse.setHttpcode(HttpStatus.BAD_REQUEST.value());
	    failureRestResponse.setErrorMessage("Invalid fields passed. Please resolve the same.");
	    
	    HashMap<String, String[]> errors = new HashMap<>();
	    String fieldName = ((DeserializerException)ex.getCause()).getField();
	    String[] errorMessage = new String[] {ex.getCause().getMessage()};
	    errors.put(fieldName, errorMessage);
	    
	    failureRestResponse.setFieldErrors(errors);
	    
	    return ResponseEntity.of(Optional.of(failureRestResponse));
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseBody
	public ResponseEntity<FailureRestResponse> handleControllerException(HttpServletRequest request, Throwable ex) {

		// default value
	    int httpCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
	    // StringWriter sw = new StringWriter();
	    // ex.printStackTrace(new PrintWriter(sw));
	    // String exceptionAsString = sw.toString();
	    
	    
	    FailureRestResponse failureRestResponse = new FailureRestResponse();
	    failureRestResponse.setStatus("Fail");
	    failureRestResponse.setHttpcode(httpCode);
	    failureRestResponse.setErrorMessage(ex.getMessage());
	    // failureRestResponse.setStackTrace(exceptionAsString);
	    log.error(ex.getMessage(), ex);
	    
	    
	    return ResponseEntity.of(Optional.of(failureRestResponse));
	 }

}
