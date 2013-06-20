package ngusd.docu.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CallTree {
	
	private Call call;
	
	@XmlElementWrapper(name = "childCallTrees")
	@XmlElement(name = "callTree")
	private List<CallTree> childCallTrees = new ArrayList<CallTree>();
	
	public Call getCall() {
		return call;
	}
	
	public void setCall(final Call call) {
		this.call = call;
	}
	
	public List<CallTree> getChildCallTrees() {
		return childCallTrees;
	}
	
	public void setChildCallTrees(final List<CallTree> childCallTrees) {
		this.childCallTrees = childCallTrees;
	}
	
	public void addChildCallTree(final CallTree callTree) {
		childCallTrees.add(callTree);
	}
	
}
