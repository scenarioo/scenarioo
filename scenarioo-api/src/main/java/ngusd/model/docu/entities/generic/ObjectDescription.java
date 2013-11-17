package ngusd.model.docu.entities.generic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * Description of a generic (application specific) object to store in the documentation.
 * 
 * May contain other childs or references to other objects in the store as child objects inside details.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ObjectDescription {
	
	private String name;
	private String type;
	private Details details = new Details();
	
	public ObjectDescription() {
	}
	
	public ObjectDescription(final String type, final String name) {
		this.type = type;
		this.name = name;
	}
	
	public void addDetail(final String key, final Object value) {
		details.put(key, value);
	}
	
}
