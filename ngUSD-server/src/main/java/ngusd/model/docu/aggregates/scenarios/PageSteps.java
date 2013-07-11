package ngusd.model.docu.aggregates.scenarios;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import ngusd.model.docu.entities.Page;
import ngusd.model.docu.entities.StepDescription;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PageSteps {
	
	private Page page;
	
	@XmlElementWrapper(name = "steps")
	@XmlElement(name = "step")
	private List<StepDescription> steps;
	
}
