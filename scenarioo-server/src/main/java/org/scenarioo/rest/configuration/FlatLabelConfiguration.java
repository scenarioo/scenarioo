package org.scenarioo.rest.configuration;

import org.scenarioo.model.configuration.LabelConfiguration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class FlatLabelConfiguration extends LabelConfiguration {
	private String name;

	public FlatLabelConfiguration() {
	}

	FlatLabelConfiguration(final String name, final LabelConfiguration configuration) {
		this.name = name;
		setForegroundColor(configuration.getForegroundColor());
		setBackgroundColor(configuration.getBackgroundColor());
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}
}
