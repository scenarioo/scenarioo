/* scenarioo-api
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

package org.scenarioo.model.docu.entities.generic;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@EqualsAndHashCode
public class Details implements Map<String, Object>, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private final Map<String, Object> properties = new HashMap<String, Object>();
	
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
	
}
