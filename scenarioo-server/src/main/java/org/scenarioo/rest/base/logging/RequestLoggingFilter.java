package org.scenarioo.rest.base.logging;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Logs every HTTP request and the method that is called by it.
 */
public class RequestLoggingFilter implements HandlerInterceptor {

	private static final Logger LOGGER = Logger.getLogger(RequestLoggingFilter.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		if(handler instanceof  HandlerMethod){
			StringBuilder message = new StringBuilder();
			message.append(request.getMethod()).append(" ").append(request.getRequestURI());
			if(StringUtils.isNotBlank(request.getQueryString())) {
				message.append("?").append(request.getQueryString());
			}
			HandlerMethod method = (HandlerMethod)handler;
			message.append(" -> ");
			message.append(method.getBeanType().getSimpleName());
			message.append(".");
			message.append(method.getMethod().getName());
			message.append("(").append(getParameterTypes(method.getMethodParameters())).append(")");
			LOGGER.debug(message.toString());
		}
		return true;
	}

	private String getParameterTypes(MethodParameter[] parameters) {
		List<String> types = new LinkedList<>();
		for (MethodParameter parameter : parameters) {
			types.add(parameter.getParameterType().getSimpleName());
		}
		return String.join(", ", types);
	}
}
