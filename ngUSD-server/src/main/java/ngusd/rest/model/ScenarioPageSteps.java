package ngusd.rest.model;

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
public class ScenarioPageSteps {
	
	private Scenario scenario;
	
	private UseCase useCase;
	
	@XmlElementWrapper(name = "pagesAndSteps")
	@XmlElement(name = "pageSteps")
	private List<PageSteps> pagesAndSteps;
	
	public Scenario getScenario() {
		return scenario;
	}
	
	public void setScenario(final Scenario scenario) {
		this.scenario = scenario;
	}
	
	public UseCase getUseCase() {
		return useCase;
	}
	
	public void setUseCase(final UseCase useCase) {
		this.useCase = useCase;
	}
	
	public List<PageSteps> getPagesAndSteps() {
		return pagesAndSteps;
	}
	
	public void setPagesAndSteps(final List<PageSteps> pagesAndSteps) {
		this.pagesAndSteps = pagesAndSteps;
	}
	
}
