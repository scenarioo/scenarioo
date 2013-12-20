/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
