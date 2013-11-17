package ngusd.model.docu.entities.generic;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import lombok.EqualsAndHashCode;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@EqualsAndHashCode
public class ObjectReference {
	
	private String name;
	private String type;
	
	public ObjectReference() {
	}
	
	public ObjectReference(final String type, final String name) {
		this.type = type;
		this.name = name;
	}
	
}
