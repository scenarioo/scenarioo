package org.scenarioo.model.docu.aggregates.scenarios;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ScenarioStatistics implements Serializable {
	
	private int numberOfPages;
	private int numberOfSteps;
	
	public int getNumberOfPages() {
		return numberOfPages;
	}
	
	public void setNumberOfPages(final int numberOfPages) {
		this.numberOfPages = numberOfPages;
	}
	
	public int getNumberOfSteps() {
		return numberOfSteps;
	}
	
	public void setNumberOfSteps(final int numberOfSteps) {
		this.numberOfSteps = numberOfSteps;
	}
	
}
