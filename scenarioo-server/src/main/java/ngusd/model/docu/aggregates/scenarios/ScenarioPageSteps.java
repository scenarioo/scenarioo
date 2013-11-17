package ngusd.model.docu.aggregates.scenarios;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.UseCase;

import lombok.Data;

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
