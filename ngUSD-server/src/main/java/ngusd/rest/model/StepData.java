package ngusd.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains all the data collected from a webtest for one step of the webtest.
 * 
 * The data is processed by the ngUSd webapplication to transform into webapps internal structure.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepData {
	
	private String pageName;
	
	private Details pageDetails = new Details();
	
	private Step step;
	
	private StepHtml html;	
	
	private CallTree callTree = new CallTree();		
	
	private StepMetadata metadata = new StepMetadata();

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public Details getPageDetails() {
		return pageDetails;
	}

	public void setPageDetails(Details pageDetails) {
		this.pageDetails = pageDetails;
	}

	public Step getStep() {
		return step;
	}

	public void setStep(Step step) {
		this.step = step;
	}
	
	public StepHtml getHtml() {
		return html;
	}
	
	public void setHtml(StepHtml html) {
		this.html = html;
	}

	public CallTree getCallTree() {
		return callTree;
	}

	public void setCallTree(CallTree callTree) {
		this.callTree = callTree;
	}

	public StepMetadata getMetadata() {
		return metadata;
	}

	public void setMetadata(StepMetadata metadata) {
		this.metadata = metadata;
	}
	
	
	
}
