package org.scenarioo.model.docu.feature.model;

import org.scenarioo.model.docu.feature.model.screenAnnotations.ScreenAnnotation;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Step implements Serializable{

	private Page page;
	private StepDescription stepDescription;
	private String stepHtml;

	@XmlElement(name = "screenAnnotation")
	@XmlElementWrapper(name = "screenAnnotations")
	private List<ScreenAnnotation> screenAnnotations = new ArrayList<ScreenAnnotation>();
}
