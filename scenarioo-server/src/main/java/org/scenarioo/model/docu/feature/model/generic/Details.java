package org.scenarioo.model.docu.feature.model.generic;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.HashMap;
import java.util.Map;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Details extends HashMap<String, Object> implements Detailable {


	public Details details;

	public Map<String, Object> getProperties() {
		return this;
	}

	public Details addDetail(final String key, final Object value) {
		this.put(key, value);
		return this;
	}

	public Object getDetail(final String key) {
		return get(key);
	}
}
