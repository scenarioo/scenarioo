package ngusd.model.docu.aggregates.usecases;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * List of all use cases with its scenarios
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class UseCaseScenariosList {
	
	private String version;
	@XmlElementWrapper(name = "list")
	@XmlElement(name = "useCaseScenarios")
	private List<UseCaseScenarios> useCaseScenarios;
}
