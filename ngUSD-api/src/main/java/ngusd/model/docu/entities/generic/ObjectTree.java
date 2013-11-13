package ngusd.model.docu.entities.generic;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ObjectTree {
	
	private Details node = new Details();
	
	private List<ObjectTree> children = new ArrayList<ObjectTree>();
	
	public void addChild(final ObjectTree child) {
		children.add(child);
	}
	
}
