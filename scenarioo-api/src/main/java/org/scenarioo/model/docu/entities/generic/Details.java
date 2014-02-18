/* scenarioo-api
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * As a special exception, the copyright holders of this library give you 
 * permission to link this library with independent modules, according 
 * to the GNU General Public License with "Classpath" exception as provided
 * in the LICENSE file that accompanied this code.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.scenarioo.model.docu.entities.generic;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Details implements Map<String, Object>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final Map<String, Object> properties = new HashMap<String, Object>();
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void addDetail(final String key, final Object value) {
		if (value != null) {
			properties.put(key, value);
		}
		else {
			properties.remove(key);
		}
	}
	
	public Object getDetail(final String key) {
		return properties.get(key);
	}
	
	@Override
	public int size() {
		return properties.size();
	}
	
	@Override
	public boolean isEmpty() {
		return properties.isEmpty();
	}
	
	@Override
	public boolean containsKey(final Object key) {
		return properties.containsKey(key);
	}
	
	@Override
	public boolean containsValue(final Object value) {
		return properties.containsValue(value);
	}
	
	@Override
	public Object get(final Object key) {
		return properties.get(key);
	}
	
	@Override
	public Object put(final String key, final Object value) {
		return properties.put(key, value);
	}
	
	@Override
	public Object remove(final Object key) {
		return properties.remove(key);
	}
	
	@Override
	public void putAll(final Map<? extends String, ? extends Object> m) {
		properties.putAll(m);
	}
	
	@Override
	public void clear() {
		properties.clear();
	}
	
	@Override
	public Set<String> keySet() {
		return properties.keySet();
	}
	
	@Override
	public Collection<Object> values() {
		return properties.values();
	}
	
	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		return properties.entrySet();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((properties == null) ? 0 : properties.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Details other = (Details) obj;
		if (properties == null) {
			if (other.properties != null) {
				return false;
			}
		} else if (!properties.equals(other.properties)) {
			return false;
		}
		return true;
	}
	
}
