package ngusd.rest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UseCase {
	
	private String name;
	private String description;
	private String status;
	
	private Map<String, Object> properties = new HashMap<>();
	
	@XmlElementWrapper(name = "scenarios")
	@XmlElement(name = "scenario")
	private List<Scenario> scenarios = new ArrayList<>();
	
	public UseCase() {
		this("", "");
	}
	
	public UseCase(final String name, final String description) {
		super();
		this.name = name;
		this.description = description;
		this.status = "";
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(final String description) {
		this.description = description;
	}
	
	public Map<String, Object> getProperties() {
		return properties;
	}
	
	public void setProperties(final Map<String, Object> properties) {
		this.properties = properties;
	}
	
	public List<Scenario> getScenarios() {
		return scenarios;
	}
	
	public void setScenarios(final List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(final String status) {
		this.status = status;
	}
	
}
