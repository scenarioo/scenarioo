package org.scenarioo.model.docu.feature.model;

import org.scenarioo.model.docu.feature.model.generic.Detailable;
import org.scenarioo.model.docu.feature.model.generic.Details;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Date;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Build implements Serializable, Detailable {

	public Details details;
	public String name;
	public String revision;
	public Date date;
	public String status;


	public String branchName;

}
