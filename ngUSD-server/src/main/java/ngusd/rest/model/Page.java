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
public class Page {
	private String name;
	private final Details details = new Details();
	
	@XmlElementWrapper(name = "steps")
	@XmlElement(name = "step")
	private final List<Step> steps = new ArrayList<Step>();
	
	public Page() {
	}
	
	public Page(final String name) {
		super();
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(final String name) {
		this.name = name;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public List<Step> getSteps() {
		return steps;
	}
	
	public void addStep(final Step step) {
		this.steps.add(step);
	}
	
}
