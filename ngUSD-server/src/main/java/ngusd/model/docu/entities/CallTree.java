package ngusd.model.docu.entities;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class CallTree {
	
	private Call call;
	
	@XmlElementWrapper(name = "childCallTrees")
	@XmlElement(name = "callTree")
	private List<CallTree> childCallTrees = new ArrayList<CallTree>();
	
	public void addChildCallTree(final CallTree callTree) {
		childCallTrees.add(callTree);
	}
	
}
