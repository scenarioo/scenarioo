package org.scenarioo.rest.application;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class ApplicationVersion {

	public ApplicationVersion() {
		// For REST
	}

	public ApplicationVersion(final String version, final String buildDate) {
		this.version = version;
		this.buildDate = buildDate;
	}

	private String version;
	private String buildDate;

	public String getVersion() {
		return version;
	}

	public String getBuildDate() {
		return buildDate;
	}

}
