package org.scenarioo.model.lastSuccessfulScenarios;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class LastSuccessfulScenario {
	
	private Date buildDate;
	
	public Date getBuildDate() {
		return buildDate;
	}
	
	public void setBuildDate(final Date buildDate) {
		this.buildDate = buildDate;
	}
	
}
