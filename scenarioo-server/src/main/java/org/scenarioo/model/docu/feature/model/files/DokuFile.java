package org.scenarioo.model.docu.feature.model.files;

import org.scenarioo.model.docu.feature.model.Link;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class DokuFile {

	public String name;
	public String content;
	public String url;
	public FileType type;

	public List<Link> links = new ArrayList<>();

}
