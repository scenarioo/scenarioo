package org.scenarioo.rest.base.exceptions;

import org.apache.log4j.Logger;
import org.scenarioo.api.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	private Logger LOGGER = Logger.getLogger(RestExceptionHandler.class);

	@ExceptionHandler(value = {ResourceNotFoundException.class})
	public ResponseEntity<String> handleResourceNotFoundException(ResourceNotFoundException exception, WebRequest request) {
		LOGGER.warn(exception.getMessage());
		return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
	}
}
