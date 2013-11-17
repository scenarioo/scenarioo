package org.scenarioo.model.docu.aggregates.usecases;

import java.util.ArrayList;
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
