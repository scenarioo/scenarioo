package scenarioo.uitest.dummy.application;

import lombok.Data;
import ngusd.model.docu.entities.generic.ObjectDescription;
import ngusd.model.docu.entities.generic.ObjectTreeNode;

/**
 * Just some example data about internal state of your application that you could collect in your UI tests to store and
 * display it in the documentation.
 * 
 * The collection of such application specific information is very specific for your application.
 * 
 * Some possibilities on how to make such information available in your UI tests:
 * <ul>
 * <li>read it from log files of your application</li>
 * <li>render a special footer on each page of your application with internal application state information (only when
 * testing), this might also be helpful for other debugging/testing purposes.</li>
 * </ul>
 */
@Data
public class ApplicationsStateData {
	
	/**
	 * The name of the current screen/page/view (however you name it in your application).
	 */
	private String pageName;
	
	/**
	 * Information about most important code parts and services called between last step and current step. The leaves of
	 * this trees are calls to other systems (like webservices, databases, etc.)
	 */
	private ObjectTreeNode<ObjectDescription> callTree;
	
	/**
	 * Probably you are simulating or mocking some backend systems or services when testing for docu generation. The
	 * configuration gives information about current simulation configuration properties.
	 */
	private ObjectDescription currentSimulationConfiguration;
	
}
