# Labels

A use case, a scenario, a step and a page in the documentation can be labeled with index words. This labels can be browsed for, e.g. search all usecases/scenarios/steps/pages that contain a specific label. Also labels can be included in link-URLs to specific pages of your scenarioo documentation. If an URL to a documentation object is somehow outdated (e.g. because a scenario or use case was renamed), the Scenarioo Viewer will try to find a fallback to a similar object (step/scenario etc.) in the documentation that matches most of the provided labels and identifiers in the URL.

Labels are a list of strings (index words) that adhere to the following regular Expression: 

**^[ a-zA-Z0-9_-]+$** 

They can be set on a Use Case, Scenario, Step (inside StepDescription) and a Page.

Example:
    ...
    <step>
    <page>
        <name>contentPage.jsp</name>
        <details/>
        <labels>
            <label>page-label1</label>
            <label>page-label2</label>
        </labels>
    </page>
    ...
