package org.scenarioo.model.configuration;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class CustomObjectTab {
	
	private String id;
	
	private String tabTitle;
	
	private List<String> objectTypesToDisplay = new ArrayList<String>();
	
	private List<CustomObjectDetailColumn> customObjectDetailColumns = new ArrayList<CustomObjectDetailColumn>();	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTabTitle() {
		return tabTitle;
	}

	public void setTabTitle(String tabTitle) {
		this.tabTitle = tabTitle;
	}

	public List<String> getObjectTypesToDisplay() {
		return objectTypesToDisplay;
	}

	public void setObjectTypesToDisplay(List<String> objectTypesToDisplay) {
		this.objectTypesToDisplay = objectTypesToDisplay;
	}

	public List<CustomObjectDetailColumn> getCustomObjectDetailColumns() {
		return customObjectDetailColumns;
	}

	public void setCustomObjectDetailColumns(
			List<CustomObjectDetailColumn> customObjectDetailColumns) {
		this.customObjectDetailColumns = customObjectDetailColumns;
	}
	
	

}
