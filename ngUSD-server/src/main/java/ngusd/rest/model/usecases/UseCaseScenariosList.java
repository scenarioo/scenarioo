package ngusd.rest.model.usecases;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * List of all use cases with its scenarios
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UseCaseScenariosList {
	
	@XmlElementWrapper(name = "list")
	@XmlElement(name = "useCaseScenarios")
	private List<UseCaseScenarios> useCaseScenarios;
	
	public List<UseCaseScenarios> getUseCaseScenarios() {
		return useCaseScenarios;
	}
	
	public void setUseCaseScenarios(final List<UseCaseScenarios> useCaseScenarios) {
		this.useCaseScenarios = useCaseScenarios;
	}
	
}
