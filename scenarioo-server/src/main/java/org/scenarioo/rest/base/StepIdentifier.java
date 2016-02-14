package org.scenarioo.rest.base;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonIgnore;

/**
 * Contains all the properties needed to identify a step unambiguously.
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class StepIdentifier {
	
	private static final String URI_ENCODING = "UTF-8";
	
	@XmlTransient
	private ScenarioIdentifier scenarioIdentifier;

	String pageName;
	int pageOccurrence;
	int stepInPageOccurrence;
	private Set<String> labels;
	
	private enum RedirectType {
		STEP, SCREENSHOT
	}

	/**
	 * Just for XML processing.
	 */
	public StepIdentifier() {
		scenarioIdentifier = new ScenarioIdentifier();
	}

	public StepIdentifier(final BuildIdentifier buildIdentifier, final String usecaseName, final String scenarioName,
			final String pageName, final int pageOccurrence, final int stepInPageOccurrence) {
		this(new ScenarioIdentifier(buildIdentifier, usecaseName, scenarioName), pageName, pageOccurrence,
				stepInPageOccurrence);
	}
	
	public StepIdentifier(final BuildIdentifier buildIdentifier, final String usecaseName, final String scenarioName,
			final String pageName, final int pageOccurrence, final int stepInPageOccurrence, final Set<String> labels) {
		this(new ScenarioIdentifier(buildIdentifier, usecaseName, scenarioName), pageName, pageOccurrence,
				stepInPageOccurrence, labels);
	}
	
	public StepIdentifier(final ScenarioIdentifier scenarioIdentifier, final String pageName, final int pageOccurrence,
			final int stepInPageOccurrence) {
		this(scenarioIdentifier, pageName, pageOccurrence, stepInPageOccurrence, null);
	}
	
	public StepIdentifier(final ScenarioIdentifier scenarioIdentifier, final String pageName, final int pageOccurrence,
			final int stepInPageOccurrence, final Set<String> labels) {
		this.scenarioIdentifier = scenarioIdentifier;
		this.pageName = pageName;
		this.pageOccurrence = pageOccurrence;
		this.stepInPageOccurrence = stepInPageOccurrence;
		this.labels = labels;
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
			final String fallbackUsecaseName, final String fallbackScenarioName, final int pageOccurrence,
			final int stepInPageOccurrence) {
		return new StepIdentifier(originalStepIdentifier.getBuildIdentifier(), fallbackUsecaseName,
				fallbackScenarioName, originalStepIdentifier.getPageName(), pageOccurrence, stepInPageOccurrence);
	}
	
	public StepIdentifier withDifferentBuildIdentifier(final BuildIdentifier buildIdentifierBeforeAliasResolution) {
		return new StepIdentifier(getScenarioIdentifier().withDifferentBuildIdentifier(
				buildIdentifierBeforeAliasResolution), pageName, pageOccurrence, stepInPageOccurrence);
	}
	
	@JsonIgnore
	public BuildIdentifier getBuildIdentifier() {
		return scenarioIdentifier.getBuildIdentifier();
	}
	
	@JsonIgnore
	public ScenarioIdentifier getScenarioIdentifier() {
		return scenarioIdentifier;
	}
	
	@JsonIgnore
	public void setScenarioIdentifier(final ScenarioIdentifier scenarioIdentifier) {
		this.scenarioIdentifier = scenarioIdentifier;
	}

	@XmlElement
	public String getBranchName() {
		return scenarioIdentifier.getBranchName();
	}
	
	public void setBranchName(final String branchName) {
		scenarioIdentifier.setBranchName(branchName);
	}

	@XmlElement
	public String getBuildName() {
		return scenarioIdentifier.getBuildName();
	}
	
	public void setBuildName(final String buildName) {
		scenarioIdentifier.setBuildName(buildName);
	}

	@XmlElement
	public String getUsecaseName() {
		return scenarioIdentifier.getUsecaseName();
	}
	
	public void setUsecaseName(final String usecaseName) {
		scenarioIdentifier.setUsecaseName(usecaseName);
	}

	@XmlElement
	public String getScenarioName() {
		return scenarioIdentifier.getScenarioName();
	}
	
	public void setScenarioName(final String scenarioName) {
		scenarioIdentifier.setScenarioName(scenarioName);
	}

	public String getPageName() {
		return pageName;
	}
	
	public void setPageName(final String pageName) {
		this.pageName = pageName;
	}

	public int getPageOccurrence() {
		return pageOccurrence;
	}
	
	public void setPageOccurrence(final int pageOccurrence) {
		this.pageOccurrence = pageOccurrence;
	}

	public int getStepInPageOccurrence() {
		return stepInPageOccurrence;
	}
	
	public void setStepInPageOccurrence(final int stepInPageOccurrence) {
		this.stepInPageOccurrence = stepInPageOccurrence;
	}

	public Set<String> getLabels() {
		return labels;
	}
	
	public void setLabels(final Set<String> labels) {
		this.labels = labels;
	}

	/**
	 * File name extension, e.g. "png" or "jpeg". Always without a dot.
	 */
	@JsonIgnore
	public URI getScreenshotUriForRedirect(final String screenshotFileNameExtension) {
		return createUriForRedirect(RedirectType.SCREENSHOT, screenshotFileNameExtension);
	}
	
	@JsonIgnore
	public URI getStepUriForRedirect() {
		return createUriForRedirect(RedirectType.STEP, null);
	}
	
	private URI createUriForRedirect(final RedirectType redirectType, final String screenshotFileNameExtension) {
		try {
			return getStepUri(redirectType, screenshotFileNameExtension);
		} catch (URISyntaxException e) {
			throw new RuntimeException("Redirect failed, can't create URI", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Encoding " + URI_ENCODING + " for Uri is not supported", e);
		}
	}
	
	private URI getStepUri(final RedirectType redirectType, final String screenshotFileNameExtension)
			throws UnsupportedEncodingException, URISyntaxException {
		StringBuilder uriBuilder = new StringBuilder();
		
		uriBuilder.append("/rest/branch/").append(encode(getBranchName()));
		uriBuilder.append("/build/").append(encode(getBuildName()));
		uriBuilder.append("/usecase/").append(encode(getUsecaseName()));
		uriBuilder.append("/scenario/").append(encode(getScenarioName()));
		uriBuilder.append("/pageName/").append(encode(getPageName()));
		uriBuilder.append("/pageOccurrence/").append(encode(Integer.toString(getPageOccurrence())));
		uriBuilder.append("/stepInPageOccurrence/").append(encode(Integer.toString(getStepInPageOccurrence())));
		
		if (RedirectType.SCREENSHOT.equals(redirectType)) {
			uriBuilder.append("/image." + screenshotFileNameExtension);
		}
		
		uriBuilder.append("?fallback=true");
		
		return new URI(uriBuilder.toString());
	}
	
	private String encode(final String urlParameter) throws UnsupportedEncodingException {
		String encoded = URLEncoder.encode(urlParameter, URI_ENCODING);
		return encoded.replace("+", "%20");
	}
	
	@Override
	public String toString() {
		return new ToStringBuilder(this).append(scenarioIdentifier).append(pageName).append(pageOccurrence)
				.append(stepInPageOccurrence).append(labels).build();
	}
	
}
