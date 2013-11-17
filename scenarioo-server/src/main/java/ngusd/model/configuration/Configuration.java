package ngusd.model.configuration;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Data;

/**
 * The configuration for the server and the client.
 * 
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
@Data()
public class Configuration {
	
	private String testDocumentationDirPath;
	
	private String defaultBranchName;
	
	private String defaultBuildName;
	
	private String scenarioPropertiesInOverview;
	
	private String applicationInformation;
	
	private Map<String, String> buildstates = new HashMap<String, String>();
	
}
