package org.scenarioo.rest.configuration;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class DiffViewerStatus {
	private boolean isGraphicsMagickAvailable;

	public boolean isGraphicsMagickAvailable() {
		return isGraphicsMagickAvailable;
	}

	public void setGraphicsMagickAvailable(boolean graphicsMagickAvailable) {
		isGraphicsMagickAvailable = graphicsMagickAvailable;
	}
}
