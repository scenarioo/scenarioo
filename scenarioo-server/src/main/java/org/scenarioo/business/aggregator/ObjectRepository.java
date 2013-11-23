package org.scenarioo.business.aggregator;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDAO;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

public class ObjectRepository {
	
	private static final Logger LOGGER = Logger.getLogger(ObjectRepository.class);
	
	private ScenarioDocuAggregationDAO dao;
	
	private String branchName;
	
	private String buildName;
	
	private final Map<ObjectReference, ObjectReferenceTreeBuilder> objectReferences = new HashMap<ObjectReference, ObjectReferenceTreeBuilder>();
	
	private final Set<String> objectTypes = new HashSet<String>();
	
	public ObjectRepository(final String branchName, final String buildName, final ScenarioDocuAggregationDAO dao) {
		this.branchName = branchName;
		this.buildName = buildName;
		this.dao = dao;
	}
	
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
			addListObjects(referencePath, (ObjectList<?>) object);
		}
		else if (object instanceof ObjectTreeNode) {
			addTreeObjects(referencePath, (ObjectTreeNode<?>) object);
		}
		
	}
	
	public void addTreeObjects(final List<ObjectReference> referencePath, final ObjectTreeNode<?> objectTree) {
		
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
			addListObjects(referencePath, objectTree.getChildren());
			referencePath.remove(referencePath.size() - 1);
		}
		else {
			addObjects(referencePath, objectTree.getDetails());
			addListObjects(referencePath, objectTree.getChildren());
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
	public void addListObjects(final List<ObjectReference> referencePath, final List<?> objects) {
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
		saveObject(object);
		addObjectReference(referencePath, ref);
		referencePath.add(ref);
		addObjects(referencePath, object.getDetails());
		referencePath.remove(referencePath.size() - 1);
	}
	
	private void saveObject(final ObjectDescription object) {
		objectTypes.add(object.getType());
		if (!dao.isObjectDescriptionSaved(branchName, buildName, object)) {
			dao.saveObjectDescription(branchName, buildName, object);
		}
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
		
		// Save page description (if not yet)
		if (page != null) {
			ObjectDescription pageDescription = new ObjectDescription("page", page.getName());
			pageDescription.setDetails(page.getDetails());
			saveObject(pageDescription);
		}
		
		// Add referenced objects from page
		if (page != null) {
			addObjects(referencePath, page.getDetails());
		}
		
	}
	
	public void calculateAndSaveObjectLists() {
		for (String type : objectTypes) {
			LOGGER.info("Writing object list for type '" + type + "' ...");
			ObjectList<ObjectDescription> objectsList = new ObjectList<ObjectDescription>();
			List<File> objectFiles = dao.getFiles().getObjectFiles(branchName, buildName, type);
			for (File file : objectFiles) {
				ObjectDescription object = dao.loadObjectDescription(file);
				objectsList.add(object);
			}
			dao.saveObjectsList(branchName, buildName, type, objectsList);
			LOGGER.info("Finished successfully witing object list for type: " + type);
		}
	}
	
	public void saveObjectIndexes() {
		LOGGER.info("Writing object repository index files ... This might take a while ...");
		for (Entry<ObjectReference, ObjectReferenceTreeBuilder> objectRefTreeBuilder : objectReferences.entrySet()) {
			ObjectReference objectRef = objectRefTreeBuilder.getKey();
			ObjectReferenceTreeBuilder referenceTreeBuilder = objectRefTreeBuilder.getValue();
			if (!dao.isObjectDescriptionSaved(branchName, buildName, objectRef.getType(), objectRef.getName())) {
				LOGGER.warn("No Object Description for object found, therefore not remembering index for this object: (type='"
						+ objectRef.getType() + ", name=" + objectRef.getName() + "').");
			} else {
				ObjectDescription object = dao.loadObjectDescription(branchName, buildName,
						objectRefTreeBuilder.getKey());
				ObjectIndex objectIndex = new ObjectIndex();
				objectIndex.setObject(object);
				ObjectTreeNode<ObjectReference> referenceTree = referenceTreeBuilder.build();
				objectIndex.setReferenceTree(referenceTree);
				dao.saveObjectIndex(branchName, buildName, objectIndex);
			}
		}
		LOGGER.info("Writing object repository index files finished (success).");
	}
	
	public void removeAnyExistingObjectData() {
		deleteDirectory(dao.getFiles().getObjectsDirectory(branchName, buildName));
	}
	
	private static void deleteDirectory(final File directory) {
		if (directory.exists()) {
			try {
				FileUtils.deleteDirectory(directory);
			} catch (IOException e) {
				throw new RuntimeException("Could not delete directory: " + directory.getAbsolutePath(), e);
			}
		}
	}
	
}
