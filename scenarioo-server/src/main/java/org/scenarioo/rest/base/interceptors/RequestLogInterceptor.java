package org.scenarioo.rest.base.interceptors;

import org.apache.log4j.Logger;
import org.jboss.resteasy.annotations.interception.ServerInterceptor;
import org.jboss.resteasy.core.ResourceMethod;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.spi.Failure;
import org.jboss.resteasy.spi.HttpRequest;
import org.jboss.resteasy.spi.interception.PostProcessInterceptor;
import org.jboss.resteasy.spi.interception.PreProcessInterceptor;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.ext.Provider;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.LinkedList;

/**
 * Logs every HTTP request and the method that is called by it.
 */
@Provider
@ServerInterceptor
public class RequestLogInterceptor implements PreProcessInterceptor{

	private static final Logger LOGGER = Logger.getLogger(RequestLogInterceptor.class);

	@Override
	public ServerResponse preProcess(HttpRequest request, ResourceMethod method) throws Failure, WebApplicationException {
		LOGGER.debug(request.getHttpMethod() + " " + request.getUri().getPath() + " -> "
			+ method.getMethod().getName() + "(" + getParameterTypes(method.getMethod()) + ")");
		return null;
	}

	private String getParameterTypes(Method method) {
		LinkedList<String> types = new LinkedList<String>();
		for (Parameter parameter : method.getParameters()) {
			types.add(parameter.getType().getSimpleName());
		}
		return String.join(", ", types);
	}

}
