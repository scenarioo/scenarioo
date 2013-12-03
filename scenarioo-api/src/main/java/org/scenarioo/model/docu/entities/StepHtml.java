package org.scenarioo.model.docu.entities;

import java.io.Serializable;

import lombok.Data;

@Data
public class StepHtml implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String htmlSource = "";
	
	public StepHtml() {
	}
	
	public StepHtml(final String htmlSource) {
		this.htmlSource = htmlSource;
	}
	
}
