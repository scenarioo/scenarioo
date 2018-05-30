package org.scenarioo.rest.base.exceptions;

import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

/**
 * This exception handler is very important to have error logs in case of runtime errors (=potential bugs)
 */
@Provider
public class RuntimeExceptionHandler implements ExceptionMapper<RuntimeException> {

	private Logger LOGGER = Logger.getLogger(ResourceNotFoundExceptionHandler.class);

	@Override
	public Response toResponse(final RuntimeException exception) {
		LOGGER.error("Runtime exception", exception);
		return Response.status(500).build();
	}
}
