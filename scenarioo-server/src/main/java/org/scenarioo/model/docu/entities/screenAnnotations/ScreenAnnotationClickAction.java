package org.scenarioo.model.docu.entities.screenAnnotations;


public enum ScreenAnnotationClickAction {

	/**
	 * Let the user navigate to the next step when he clicks on the annotation.
	 */
	TO_NEXT_STEP,

	/**
	 * Let the user navigate to a URL specified in property 'clickActionUrl' when he clicks on the annotation.
	 *
	 * The URL will be opened in a separate browser tab.
	 */
	TO_URL;

}
