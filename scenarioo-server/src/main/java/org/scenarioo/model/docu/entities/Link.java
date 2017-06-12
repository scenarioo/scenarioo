package org.scenarioo.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {

	public String name;
	public String url;

	public Link() {
	}

	public Link(String name, String url) {
		this.name = name;
		this.url = url;
	}

}
