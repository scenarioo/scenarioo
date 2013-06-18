package ngusd.rest.model;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Details {
	private final Map<String, Object> properties = new HashMap<String, Object>();
	
	public Map<String, Object> getDetails() {
		return properties;
	}
	
	public void addDetail(final String key, final Object value) {
		properties.put(key, value);
	}
	
	public Object getDetail(final String key) {
		return properties.get(key);
	}
}
