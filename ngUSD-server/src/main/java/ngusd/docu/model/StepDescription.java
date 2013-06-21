package ngusd.docu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepDescription {
	
	private int index = 0;
	private String title = "";
	private String screenshotURL;
	private final Details details = new Details();
	
	public StepDescription() {
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(final String title) {
		this.title = title;
	}
	
	public String getScreenshotURL() {
		return screenshotURL;
	}
	
	public void setScreenshotURL(final String screenshotURL) {
		this.screenshotURL = screenshotURL;
	}
	
	public Details getDetails() {
		return details;
	}
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(final int index) {
		this.index = index;
	}
	
}
