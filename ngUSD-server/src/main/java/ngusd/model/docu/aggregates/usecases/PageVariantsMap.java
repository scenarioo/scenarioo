package ngusd.model.docu.aggregates.usecases;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;
import ngusd.model.docu.entities.PageVariants;

/**
 * Map of all page variants
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PageVariantsMap {
	
	public PageVariantsMap() {
	}
	
	public PageVariantsMap(final Map<String, PageVariants> mapPageToOccurences,
			final Map<String, PageVariants> mapPageToSteps) {
		this.mapPageToOccurences = mapPageToOccurences;
		this.mapPageToSteps = mapPageToSteps;
	}
	
	Map<String, PageVariants> mapPageToOccurences;
	Map<String, PageVariants> mapPageToSteps;
}
