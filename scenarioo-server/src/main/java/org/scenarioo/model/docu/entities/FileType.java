package org.scenarioo.model.docu.entities;

import javax.xml.bind.annotation.XmlEnum;

@XmlEnum
public enum  FileType {

	JAVA("Java", ".java"),
	MARKDOWN("Markdown", ".md"),
	CSHARP("C #", ".cs"),
	JAVASCRIPT("JavaScript", ".js"),
	GHERKIN("Gherkin", ".feature"),
	;


	private final String type;
	private final String fileExtension;

	FileType(String type, String fileExtension){
		this.type = type;
		this.fileExtension = fileExtension;
	}

}
