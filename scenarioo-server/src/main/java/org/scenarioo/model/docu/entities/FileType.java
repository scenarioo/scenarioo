package org.scenarioo.model.docu.entities;

import javax.xml.bind.annotation.XmlEnum;
import java.io.File;

@XmlEnum
public enum  FileType {

	JAVA("Java", ".java"),
	MARKDOWN("Markdown", ".md"),
	CSHARP("C #", ".cs"),
	JAVASCRIPT("JavaScript", ".js"),
	GHERKIN("Gherkin", ".feature");

	private final String type;
	private final String fileExtension;

	FileType(String type, String fileExtension){
		this.type = type;
		this.fileExtension = fileExtension;
	}

	public static FileType getFromFileEnding(File file) {
		String lowercaseFileName = file.getName().toLowerCase();
		for (FileType type : values()) {
			if (lowercaseFileName.endsWith(type.fileExtension)) {
				return type;
			}
		}
		return null;
	}

}
