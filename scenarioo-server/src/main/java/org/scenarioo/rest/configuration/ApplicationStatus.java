package org.scenarioo.rest.configuration;

import org.scenarioo.dao.version.ApplicationVersion;
import org.scenarioo.model.configuration.Configuration;
import org.scenarioo.rest.search.SearchEngineStatus;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ApplicationStatus {

    private Configuration configuration;
    private DiffViewerStatus diffViewerStatus;
    private SearchEngineStatus searchEngineStatus;
    private ApplicationVersion version;

    public ApplicationStatus() {
    }

    public ApplicationStatus(Configuration configuration, DiffViewerStatus diffViewerStatus, SearchEngineStatus searchEngineStatus, ApplicationVersion version) {
        this.configuration = configuration;
        this.diffViewerStatus = diffViewerStatus;
        this.searchEngineStatus = searchEngineStatus;
        this.version = version;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public DiffViewerStatus getDiffViewerStatus() {
        return diffViewerStatus;
    }

    public SearchEngineStatus getSearchEngineStatus() {
        return searchEngineStatus;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public void setDiffViewerStatus(DiffViewerStatus diffViewerStatus) {
        this.diffViewerStatus = diffViewerStatus;
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
