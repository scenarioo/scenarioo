package org.scenarioo.model.docu.aggregates.usecases;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * Counter for all page variants
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data
public class PageVariantsCounter {
	
	private Map<String, Integer> counters;
	
	public PageVariantsCounter() {
	}
	
	public PageVariantsCounter(final HashMap<String, Integer> counters) {
		this.counters = counters;
	}
}
