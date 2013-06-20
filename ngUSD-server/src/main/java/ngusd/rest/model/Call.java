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
public class Call {
	
	private String type;
	
	private String name;
	
	private String request;
	
	private String response;
	
	private Details details = new Details();	
		
	@XmlElementWrapper(name = "childCalls")
	@XmlElement(name = "call")	
	private List<Call> childCalls = new ArrayList<Call>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public Details getDetails() {
		return details;
	}

	public void setDetails(Details details) {
		this.details = details;
	}

	public List<Call> getChildCalls() {
		return childCalls;
	}

	public void setChildCalls(List<Call> childCalls) {
		this.childCalls = childCalls;
	}
	
	public void addChildCall(Call call) {
		childCalls.add(call);
	}
	
}
