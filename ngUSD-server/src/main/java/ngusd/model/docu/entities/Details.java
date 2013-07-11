package ngusd.model.docu.entities;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class Details {
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
}
