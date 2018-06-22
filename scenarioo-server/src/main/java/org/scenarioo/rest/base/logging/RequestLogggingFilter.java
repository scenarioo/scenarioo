package org.scenarioo.rest.base.logging;

import org.apache.log4j.Logger;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;
import java.util.List;

/**
 * Logs every HTTP request and the method that is called by it.
 */
@Provider
public class RequestLogggingFilter implements ContainerRequestFilter {

	private static final Logger LOGGER = Logger.getLogger(RequestLogggingFilter.class);

	@Context
	ResourceInfo resourceInfo;

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		LOGGER.debug(requestContext.getMethod() + " " + requestContext.getUriInfo().getPath() + " -> "
			+ resourceInfo.getResourceClass().getSimpleName()
			+ resourceInfo.getResourceMethod().getName()
			+ "(" + getParameterTypes(resourceInfo.getResourceMethod()) + ")");
	}

	private String getParameterTypes(Method method) {
		List<String> types = new LinkedList<String>();
		for (Parameter parameter : method.getParameters()) {
			types.add(parameter.getType().getSimpleName());
		}
		return String.join(", ", types);
	}
}
