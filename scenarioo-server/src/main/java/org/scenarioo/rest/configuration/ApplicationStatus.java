package org.scenarioo.rest.configuration;

import org.scenarioo.dao.version.ApplicationVersion;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.rest.search.SearchEngineStatus;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationStatus {

    private Configuration configuration;
    private SearchEngineStatus searchEngineStatus;
    private ApplicationVersion version;

    public Configuration getConfiguration() {
        return configuration;
    }


    public SearchEngineStatus getSearchEngineStatus() {
        return searchEngineStatus;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setSearchEngineStatus(SearchEngineStatus searchEngineStatus) {
        this.searchEngineStatus = searchEngineStatus;
    }

    public ApplicationVersion getVersion() {
        return version;
    }

    public void setVersion(ApplicationVersion version) {
        this.version = version;
    }
}
