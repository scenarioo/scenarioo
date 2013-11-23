package org.scenarioo.rest.context;

import javax.ws.rs.Produces;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.apache.log4j.Logger;
import org.scenarioo.api.util.xml.ScenarioDocuXMLUtil;

@Provider
@Produces({ "application/xml", "application/json" })
public class ScenarioDocuJAXBContextResolver implements ContextResolver<JAXBContext> {
	
	private static final Logger LOGGER = Logger.getLogger(ScenarioDocuJAXBContextResolver.class);
	
	@Override
	public JAXBContext getContext(final Class<?> type) {
		try {
			LOGGER.info("Creating JAXB context for type: " + type.getName());
			return ScenarioDocuXMLUtil.createJAXBContext(type);
		} catch (JAXBException e) {
			throw new IllegalStateException("Could not create JAX context for type" + type.getName(), e);
		}
	}
}
