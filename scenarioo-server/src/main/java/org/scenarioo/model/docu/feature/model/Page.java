package org.scenarioo.model.docu.feature.model;

import org.scenarioo.model.docu.feature.model.generic.Detailable;
import org.scenarioo.model.docu.feature.model.generic.Details;
import org.scenarioo.model.docu.feature.model.lable.Label;
import org.scenarioo.model.docu.feature.model.lable.Labelable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Set;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Page implements Serializable, Labelable, Detailable {
	public Set<Label> labels;
	public Details details;
	public String name;
}
