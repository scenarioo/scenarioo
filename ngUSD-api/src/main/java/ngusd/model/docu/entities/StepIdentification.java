package ngusd.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class StepIdentification {
	
	public StepIdentification() {
	}
	
	public StepIdentification(final String useCaseName, final String scenarioName, final String pageName,
			final int index,
			final int occurence, final int relativeIndex) {
		this.useCaseName = useCaseName;
		this.scenarioName = scenarioName;
		this.pageName = pageName;
		this.index = index;
		this.occurence = occurence;
		this.relativeIndex = relativeIndex;
	}
	
	private String useCaseName;
	private String scenarioName;
	private String pageName;
	private int index;
	private int occurence;
	private int relativeIndex;
}
