package org.scenarioo.rest.objectRepository;

public class StepReference {


    private final String stepDetailUrl;
    private final String screenShotUrl;

    public StepReference(String stepDetailUrl, String screenShotUrl) {
        this.stepDetailUrl = stepDetailUrl;
        this.screenShotUrl = screenShotUrl;
    }

    public String getStepDetailUrl() {
        return stepDetailUrl;
    }

    public String getScreenShotUrl() {
        return screenShotUrl;
    }
}

