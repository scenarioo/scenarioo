package ngusd.rest.model.usecases;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import ngusd.docu.model.Scenario;
import ngusd.docu.model.UseCase;

/**
 * Represents a use case with all its scenarios
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UseCaseScenarios {
	
	private UseCase useCase;
	
	@XmlElementWrapper(name = "scenarios")
	@XmlElement(name = "scenario")
	private List<Scenario> scenarios = new ArrayList<Scenario>();
	
}
