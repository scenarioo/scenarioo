package org.scenarioo.rest.request;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Contains all the properties needed to identify a step unambiguously.
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class StepIdentifier {
	
	private static final String URI_ENCODING = "UTF-8";
	private final ScenarioIdentifier scenarioIdentifier;
	final String pageName;
	final int pageOccurrence;
	final int stepInPageOccurrence;
	
	public StepIdentifier(final BuildIdentifier buildIdentifier, final String usecaseName, final String scenarioName,
			final String pageName, final int pageOccurrence, final int stepInPageOccurrence) {
		this(new ScenarioIdentifier(buildIdentifier, usecaseName, scenarioName), pageName, pageOccurrence,
				stepInPageOccurrence);
	}
	
	public StepIdentifier(final ScenarioIdentifier scenarioIdentifier, final String pageName, final int pageOccurrence,
			final int stepInPageOccurrence) {
		this.scenarioIdentifier = scenarioIdentifier;
		this.pageName = pageName;
		this.pageOccurrence = pageOccurrence;
		this.stepInPageOccurrence = stepInPageOccurrence;
	}
	
	public static StepIdentifier withDifferentStepInPageOccurrence(final StepIdentifier stepIdentifier,
			final int stepInPageOccurrence) {
		return new StepIdentifier(stepIdentifier.getScenarioIdentifier(), stepIdentifier.getPageName(),
				stepIdentifier.getPageOccurrence(), stepInPageOccurrence);
	}
	
	public static StepIdentifier withDifferentIds(final StepIdentifier stepIdentifier, final int pageOccurrence,
			final int stepInPageOccurrence) {
		return new StepIdentifier(stepIdentifier.getScenarioIdentifier(), stepIdentifier.getPageName(), pageOccurrence,
				stepInPageOccurrence);
	}
	
	/**
	 * Returns the same page in a different scenario of the same use case.
	 */
	public static StepIdentifier forFallBackScenario(final StepIdentifier originalStepIdentifier,
			final String fallbackUsecaseName, final String fallbackScenarioName) {
		return new StepIdentifier(originalStepIdentifier.getBuildIdentifier(), fallbackUsecaseName,
				fallbackScenarioName, originalStepIdentifier.getPageName(), 0, 0);
	}
	
	public BuildIdentifier getBuildIdentifier() {
		return scenarioIdentifier.getBuildIdentifier();
	}
	
	public ScenarioIdentifier getScenarioIdentifier() {
		return scenarioIdentifier;
	}
	
	public String getBranchName() {
		return scenarioIdentifier.getBuildIdentifier().getBranchName();
	}
	
	public String getBuildName() {
		return scenarioIdentifier.getBuildIdentifier().getBuildName();
	}
	
	public String getUsecaseName() {
		return scenarioIdentifier.getUsecaseName();
	}
	
	public String getScenarioName() {
		return scenarioIdentifier.getScenarioName();
	}
	
	public String getPageName() {
		return pageName;
	}
	
	public int getPageOccurrence() {
		return pageOccurrence;
	}
	
	public int getStepInPageOccurrence() {
		return stepInPageOccurrence;
	}
	
	public URI getScreenshotUriForRedirect() {
		try {
			StringBuilder uriBuilder = new StringBuilder();
			uriBuilder.append("/rest/branch/").append(encode(getBranchName()));
			uriBuilder.append("/build/").append(encode(getBuildName()));
			uriBuilder.append("/usecase/").append(encode(getUsecaseName()));
			uriBuilder.append("/scenario/").append(encode(getScenarioName()));
			uriBuilder.append("/pageName/").append(encode(getPageName()));
			uriBuilder.append("/pageOccurrence/").append(encode(Integer.toString(getPageOccurrence())));
			uriBuilder.append("/stepInPageOccurrence/").append(encode(Integer.toString(getStepInPageOccurrence())));
			uriBuilder.append(".png?fallback=true");
			return new URI(uriBuilder.toString());
		} catch (URISyntaxException e) {
			throw new RuntimeException("Redirect failed, can't create URI", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding " + URI_ENCODING + " for Uri is not supported", e);
		}
	}
	
	private String encode(final String urlParameter) throws UnsupportedEncodingException {
		String encoded = URLEncoder.encode(urlParameter, URI_ENCODING);
		return encoded.replace("+", "%20");
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(scenarioIdentifier).append(pageName).append(pageOccurrence)
				.append(stepInPageOccurrence).build();
	}
	
}
