package ngusd.model.docu.aggregates.scenarios;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.StepDescription;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PageSteps {
	
	private Page page;
	
	@XmlElementWrapper(name = "steps")
	@XmlElement(name = "step")
	private List<StepDescription> steps;
	
}
