package ngusd.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Step {
	
	private String screenshotURL;
	@XmlElementWrapper(name = "metadatas")
	@XmlElement(name = "metadata")
	private final List<Metadata> metadatas = new ArrayList<Metadata>();
	private final Details details = new Details();
	
	public Step() {
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
	
	public List<Metadata> getMetadata() {
		return metadatas;
	}
	
}
