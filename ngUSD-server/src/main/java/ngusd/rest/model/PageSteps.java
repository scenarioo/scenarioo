package ngusd.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ngusd.docu.model.Page;
import ngusd.docu.model.StepDescription;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class PageSteps {
	
	private Page page;
	
	@XmlElementWrapper(name = "steps")
	@XmlElement(name = "step")
	private List<StepDescription> steps;
	
	public Page getPage() {
		return page;
	}
	
	public void setPage(final Page page) {
		this.page = page;
	}
	
	public List<StepDescription> getSteps() {
		return steps;
	}
	
	public void setSteps(final List<StepDescription> steps) {
		this.steps = steps;
	}
	
}
