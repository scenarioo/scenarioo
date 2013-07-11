package ngusd.model.docu.aggregates.scenarios;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import ngusd.model.docu.entities.Scenario;
import ngusd.model.docu.entities.UseCase;

/**
 * Represents a scenario of a usecase with all its pages and steps.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ScenarioPageSteps {
	
	private Scenario scenario;
	
	private UseCase useCase;
	
	@XmlElementWrapper(name = "pagesAndSteps")
	@XmlElement(name = "pageSteps")
	private List<PageSteps> pagesAndSteps;
	
}
