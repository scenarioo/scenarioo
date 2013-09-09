package ngusd.rest.exceptions;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import ngusd.dao.filesystem.ResourceNotFoundException;

import com.sun.istack.logging.Logger;

@Provider
public class ExceptionHandler implements ExceptionMapper<ResourceNotFoundException> {
	
	Logger LOGGER = Logger.getLogger(ExceptionHandler.class);
	
	@Override
	public Response toResponse(final ResourceNotFoundException exception) {
		LOGGER.info("Resource not found", exception);
		return Response.status(500).build();
	}
}
