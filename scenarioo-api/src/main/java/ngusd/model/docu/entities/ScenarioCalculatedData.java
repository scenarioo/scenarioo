package ngusd.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * Special calculated data for a scenario, this data gets calculated by the Scenarioo webaplication when generated docu
 * is imported automatically, therefore you do not have to fill this data when generating documentation.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class ScenarioCalculatedData {
	
	private int numberOfPages;
	private int numberOfSteps;
	
}
