package ngusd.aggregator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ngusd.model.docu.entities.Page;
import ngusd.model.docu.entities.Scenario;
import ngusd.model.docu.entities.Step;
import ngusd.model.docu.entities.generic.Details;
import ngusd.model.docu.entities.generic.ObjectDescription;
import ngusd.model.docu.entities.generic.ObjectList;
import ngusd.model.docu.entities.generic.ObjectReference;
import ngusd.model.docu.entities.generic.ObjectTreeNode;

public class ObjectRepository {
	
	private final Map<ObjectReference, ObjectDescription> objects = new HashMap<ObjectReference, ObjectDescription>();
	
	private final Map<ObjectReference, ObjectReferenceTreeBuilder> objectReferences = new HashMap<ObjectReference, ObjectReferenceTreeBuilder>();
	
	/**
	 * Add all objects inside the passed generic object to the object repository for later saving.
	 */
	public void addObject(final List<ObjectReference> referencePath, final Object object) {
		if (object instanceof ObjectDescription) {
			addObject(referencePath, (ObjectDescription) object);
		}
		else if (object instanceof ObjectReference) {
			addObjectReference(referencePath, (ObjectReference) object);
		}
		else if (object instanceof Details) {
			addObjects(referencePath, (Details) object);
		}
		else if (object instanceof ObjectList) {
			addObjects(referencePath, (ObjectList<?>) object);
		}
		else if (object instanceof ObjectTreeNode) {
			addObjects(referencePath, (ObjectTreeNode<?>) object);
		}
		
	}
	
	public void addObjects(final List<ObjectReference> referencePath, final ObjectTreeNode<?> objectTree) {
		
		// Add node
		Object node = objectTree.getItem();
		addObject(referencePath, node);
		
		// Is there a node object to add to the referencePath?
		ObjectReference nodeRef = null;
		if (node instanceof ObjectDescription) {
			ObjectDescription objectDesc = (ObjectDescription) node;
			nodeRef = new ObjectReference(objectDesc.getType(), objectDesc.getName());
		} else if (node instanceof ObjectReference) {
			nodeRef = (ObjectReference) node;
		}
		
		// Add children and details with correct path
		if (nodeRef != null) {
			referencePath.add(nodeRef);
			addObjects(referencePath, objectTree.getDetails());
			addObjects(referencePath, objectTree.getChildren());
			referencePath.remove(referencePath.size() - 1);
		}
		else {
			addObjects(referencePath, objectTree.getDetails());
			addObjects(referencePath, objectTree.getChildren());
		}
	}
	
	/**
	 * Add objects in the passed list of objects.
	 * 
	 * Recursively resolves all subobjects, if any.
	 * 
	 * @param referencePath
	 *            the path of objects that referenced these list.
	 */
	public void addObjects(final List<ObjectReference> referencePath, final List<?> objects) {
		for (Object object : objects) {
			addObject(referencePath, object);
		}
	}
	
	/**
	 * Add objects in the passed details map to the object repository for later saving.
	 * 
	 * Recursively resolves all subobjects, if any.
	 * 
	 * @param referencePath
	 *            the path of objects that referenced these details.
	 */
	public void addObjects(final List<ObjectReference> referencePath, final Details details) {
		for (Entry<String, Object> entry : details.getProperties().entrySet()) {
			addObject(referencePath, entry.getValue());
		}
	}
	
	/**
	 * Add an object itself and all subobjects inside the internal details map to the object repository for later
	 * saving.
	 * 
	 * Recursively resolves all subobjects, if any.
	 * 
	 * @param referencePath
	 *            the path of objects that referenced these details.
	 */
	public void addObject(final List<ObjectReference> referencePath, final ObjectDescription object) {
		ObjectReference ref = new ObjectReference(object.getType(), object.getName());
		objects.put(ref, object);
		addObjectReference(referencePath, ref);
		referencePath.add(ref);
		addObjects(referencePath, object.getDetails());
		referencePath.remove(referencePath.size() - 1);
	}
	
	/**
	 * Put the object reference to an object into the objectReferences.
	 */
	public void addObjectReference(final List<ObjectReference> referencePath, final ObjectReference ref) {
		ObjectReferenceTreeBuilder refTreeBuilder = objectReferences.get(ref);
		if (refTreeBuilder == null) {
			refTreeBuilder = new ObjectReferenceTreeBuilder(ref);
			objectReferences.put(ref, refTreeBuilder);
		}
		refTreeBuilder.addPath(referencePath);
	}
	
	public List<ObjectReference> createPath(final ObjectReference objectReference) {
		List<ObjectReference> result = new ArrayList<ObjectReference>(1);
		result.add(objectReference);
		return result;
	}
	
	public List<ObjectReference> extendPath(final List<ObjectReference> referencePath,
			final ObjectReference objectReference) {
		List<ObjectReference> result = new ArrayList<ObjectReference>(referencePath.size() + 1);
		result.addAll(referencePath);
		result.add(objectReference);
		return result;
	}
	
	public List<ObjectReference> addReferencedScenarioObjects(List<ObjectReference> referencePath,
			final Scenario scenario) {
		referencePath = extendPath(referencePath, new ObjectReference("scenario", scenario.getName()));
		addObjects(referencePath, scenario.getDetails());
		return referencePath;
	}
	
	public void addReferencedStepObjects(List<ObjectReference> referencePath, final Step step) {
		
		// Page occurence in scenario
		Page page = step.getPage();
		if (page != null) {
			
			// Page occurence reference:
			ObjectReference pageOccurenceRef = new ObjectReference("pageOccurence", page.getName() + "/"
					+ step.getStepDescription().getOccurence());
			referencePath = extendPath(referencePath, pageOccurenceRef);
			
			// add page content
			addPage(referencePath, page);
		}
		
		// Add referenced objects from step
		ObjectReference stepReference = new ObjectReference("step", Integer.toString(step.getStepDescription()
				.getIndex()));
		referencePath = extendPath(referencePath, stepReference);
		addObjects(referencePath, step.getStepDescription().getDetails());
		addObjects(referencePath, step.getMetadata().getDetails());
		
	}
	
	/**
	 * Add description of a page and all referenced objects
	 */
	private void addPage(List<ObjectReference> referencePath, final Page page) {
		
		// Page reference
		ObjectReference pageReference = new ObjectReference("page", page.getName());
		referencePath = extendPath(referencePath, pageReference);
		addObjectReference(referencePath, pageReference);
		
		// Page description (if not yet)
		if (page != null && !objects.containsKey(pageReference)) {
			ObjectDescription pageDescription = new ObjectDescription("page", page.getName());
			pageDescription.setDetails(page.getDetails());
		}
		
		// Add referenced objects from page
		if (page != null) {
			addObjects(referencePath, page.getDetails());
		}
		
	}
}
