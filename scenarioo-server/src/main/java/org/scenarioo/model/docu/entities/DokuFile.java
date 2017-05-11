package org.scenarioo.model.docu.entities;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DokuFile {

	public String name;
	public String url;
	public FileType type;

	public List<Link> links = new ArrayList<Link>();

	public DokuFile(){}

	public DokuFile(String name, String url, FileType type){
		this.name=name;
		this.url=url;
		this.type=type;
	}

	public void addLink(Link link){
		links.add(link);
	}

}
