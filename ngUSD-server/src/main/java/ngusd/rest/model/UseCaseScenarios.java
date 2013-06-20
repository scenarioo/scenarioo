package ngusd.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import ngusd.docu.model.Scenario;
import ngusd.docu.model.UseCase;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class UseCaseScenarios {
	
	private UseCase useCase;
	
	@XmlElementWrapper(name = "scenarios")
	@XmlElement(name = "scenario")
	private List<Scenario> scenarios = new ArrayList<Scenario>();
	
	public UseCase getUseCase() {
		return useCase;
	}
	
	public void setUseCase(final UseCase useCase) {
		this.useCase = useCase;
	}
	
	public List<Scenario> getScenarios() {
		return scenarios;
	}
	
	public void setScenarios(final List<Scenario> scenarios) {
		this.scenarios = scenarios;
	}
	
}
