package ngusd.docu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Call {
	
	private String type;
	
	private String name;
	
	private String request;
	
	private String response;
	
	private Details details = new Details();
	
	public String getType() {
		return type;
	}
	
	public void setType(final String type) {
		this.type = type;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getRequest() {
		return request;
	}
	
	public void setRequest(final String request) {
		this.request = request;
	}
	
	public String getResponse() {
		return response;
	}
	
	public void setResponse(final String response) {
		this.response = response;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public void setDetails(final Details details) {
		this.details = details;
	}
	
}
