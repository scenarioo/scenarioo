package org.scenarioo.model.docu.entities.generic;

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
public class Details implements Map<String, Object> {
	
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
