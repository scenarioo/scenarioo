package ngusd.rest.model;

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
	
	@XmlElementWrapper(name = "calls")
	@XmlElement(name = "call")
	private List<Call> calls = new ArrayList<Call>();
	
	public List<Call> getCalls() {
		return calls;
	}
	
	public void setCalls(List<Call> calls) {
		this.calls = calls;
	}
	
	public void addCall(Call call) {
		calls.add(call);
	}

}
