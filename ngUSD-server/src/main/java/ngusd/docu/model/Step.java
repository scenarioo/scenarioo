package ngusd.docu.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Contains all the data collected from a webtest for one step of the webtest.
 * 
 * The data is processed by the ngUSd webapplication to transform into webapps
 * internal structure.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Step {
	
	private Page page;
	
	private StepDescription step;
	
	private StepHtml html;
	
	private CallTree callTree = new CallTree();
	
	private StepMetadata metadata = new StepMetadata();
	
	public Page getPage() {
		return page;
	}
	
	public void setPage(final Page page) {
		this.page = page;
	}
	
	public StepDescription getStep() {
		return step;
	}
	
	public void setStep(final StepDescription step) {
		this.step = step;
	}
	
	public StepHtml getHtml() {
		return html;
	}
	
	public void setHtml(final StepHtml html) {
		this.html = html;
	}
	
	public CallTree getCallTree() {
		return callTree;
	}
	
	public void setCallTree(final CallTree callTree) {
		this.callTree = callTree;
	}
	
	public StepMetadata getMetadata() {
		return metadata;
	}
	
	public void setMetadata(final StepMetadata metadata) {
		this.metadata = metadata;
	}
	
}
