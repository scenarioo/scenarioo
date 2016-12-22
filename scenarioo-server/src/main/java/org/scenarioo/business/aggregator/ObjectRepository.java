/* scenarioo-server
 * Copyright (C) 2014, scenarioo.org Development Team
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
import org.scenarioo.business.aggregator.customTabs.CustomObjectTabsAggregator;
import org.scenarioo.dao.aggregates.ScenarioDocuAggregationDao;
import org.scenarioo.model.docu.aggregates.objects.ObjectIndex;
import org.scenarioo.model.docu.aggregates.steps.StepLink;
import org.scenarioo.model.docu.entities.Labels;
import org.scenarioo.model.docu.entities.Page;
import org.scenarioo.model.docu.entities.Scenario;
import org.scenarioo.model.docu.entities.Step;
import org.scenarioo.model.docu.entities.UseCase;
import org.scenarioo.model.docu.entities.generic.Details;
import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectList;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;
import org.scenarioo.model.docu.entities.screenAnnotations.ScreenAnnotation;
import org.scenarioo.repository.ConfigurationRepository;
import org.scenarioo.repository.RepositoryLocator;
import org.scenarioo.rest.base.BuildIdentifier;

public class ObjectRepository {
	
	private static final Logger LOGGER = Logger.getLogger(ObjectRepository.class);
	
	private final ConfigurationRepository configurationRepository = RepositoryLocator.INSTANCE
			.getConfigurationRepository();
	
	private final ScenarioDocuAggregationDao dao;
	
	private final BuildIdentifier buildIdentifier;
	
	private final Map<ObjectReference, ObjectReferenceTreeBuilder> objectReferences = new HashMap<ObjectReference, ObjectReferenceTreeBuilder>();
	
	private final Set<String> objectTypes = new HashSet<String>();
	
	private final Map<ObjectReference, ObjectReference> objectReferencePool = new HashMap<ObjectReference, ObjectReference>(
			100000);
	
	private final CustomObjectTabsAggregator customObjectTabsAggregator;
	
	public ObjectRepository(final BuildIdentifier buildIdentifier, final ScenarioDocuAggregationDao dao) {
		this.buildIdentifier = buildIdentifier;
		this.dao = dao;
		customObjectTabsAggregator = new CustomObjectTabsAggregator(configurationRepository.getConfiguration()
				.getCustomObjectTabs(), dao, buildIdentifier);
	}
	
	/**
	 * Add all objects inside the passed generic object to the object repository for later saving.
	 */
	private void addObject(final List<ObjectReference> referencePath, final Object object) {
		if (object instanceof ObjectDescription) {
			addObject(referencePath, (ObjectDescription) object);
		} else if (object instanceof ObjectReference) {
			addObjectReference(referencePath, (ObjectReference) object);
		} else if (object instanceof Details) {
			addObjects(referencePath, (Details) object);
		} else if (object instanceof ObjectList) {
			addListObjects(referencePath, (ObjectList<?>) object);
		} else if (object instanceof ObjectTreeNode) {
			addTreeObjects(referencePath, (ObjectTreeNode<?>) object);
		}
	}
	
	private void addTreeObjects(final List<ObjectReference> referencePath, final ObjectTreeNode<?> objectTree) {
		
		// Add node
		Object node = objectTree.getItem();
		addObject(referencePath, node);
		
		// Is there a node object to add to the referencePath?
		ObjectReference nodeRef = null;
		if (node instanceof ObjectDescription) {
			ObjectDescription objectDesc = (ObjectDescription) node;
			nodeRef = createObjectReference(objectDesc.getType(), objectDesc.getName());
		} else if (node instanceof ObjectReference) {
			nodeRef = createObjectReference((ObjectReference) node);
		}
		
		// Add children and details with correct path
		if (nodeRef != null) {
			referencePath.add(nodeRef);
			addObjects(referencePath, objectTree.getDetails());
			addListObjects(referencePath, objectTree.getChildren());
			referencePath.remove(referencePath.size() - 1);
		} else {
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
	private void addListObjects(final List<ObjectReference> referencePath, final List<?> objects) {
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
	private void addObjects(final List<ObjectReference> referencePath, final Details details) {
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
	private void addObject(final List<ObjectReference> referencePath, final ObjectDescription object) {
		addObject(referencePath, object, null);
	}
	
	/**
	 * Add an object itself and all subobjects inside the internal details map to the object repository for later
	 * saving.
	 * 
	 * Recursively resolves all subobjects, if any.
	 * 
	 * @param referencePath
	 *            the path of objects that referenced these details.
	 * @param labels
	 *            labels that were available on the same object, to add too.
	 */
	private void addObject(final List<ObjectReference> referencePath, final ObjectDescription object,
			final Labels labels) {
		
		customObjectTabsAggregator.aggregateRelevantObjectIntoCustomObjectTabTrees(referencePath, object);
		
		ObjectReference ref = createObjectReference(object.getType(), object.getName());
		saveObject(object);
		addObjectReference(referencePath, ref);
		referencePath.add(ref);
		addObjects(referencePath, object.getDetails());
		if (labels != null) {
			addLabels(referencePath, labels);
		}
		referencePath.remove(referencePath.size() - 1);
	}
	
	/**
	 * Create reference or get it from pool if already available. This is done just to avoid out of memory because of a
	 * lot of same references loaded from xml files.
	 */
	private ObjectReference createObjectReference(final ObjectReference node) {
		return createObjectReference(node.getType(), node.getName());
	}
	
	/**
	 * Create reference or get it from pool if already available. This is done just to avoid out of memory because of a
	 * lot of same references loaded from xml files.
	 */
	private ObjectReference createObjectReference(final String type, final String name) {
		ObjectReference newRef = new ObjectReference(type, name);
		ObjectReference existingRef = objectReferencePool.get(newRef);
		if (existingRef != null) {
			return existingRef;
		} else {
			objectReferencePool.put(newRef, newRef);
			if (objectReferencePool.size() % 1000 == 0) {
				LOGGER.info("******* Added another 1000 object references, objects in total: "
						+ objectReferencePool.size());
			}
			return newRef;
		}
	}
	
	private ObjectReference createObjectReference(final String type, final String name, final Labels labels) {
		ObjectReferenceWithLabels newRef = new ObjectReferenceWithLabels(type, name, labels);
		ObjectReference existingRef = objectReferencePool.get(newRef);
		if (existingRef != null) {
			return existingRef;
		} else {
			objectReferencePool.put(newRef, newRef);
			if (objectReferencePool.size() % 1000 == 0) {
				LOGGER.info("******* Added another 1000 object references, objects in total: "
						+ objectReferencePool.size());
			}
			return newRef;
		}
	}
	
	private void saveObject(final ObjectDescription object) {
		objectTypes.add(object.getType());
		if (!dao.isObjectDescriptionSaved(buildIdentifier, object)) {
			dao.saveObjectDescription(buildIdentifier, object);
		}
	}
	
	/**
	 * Put the object reference to an object into the objectReferences.
	 */
	private void addObjectReference(final List<ObjectReference> referencePath, final ObjectReference ref) {
		ObjectReferenceTreeBuilder refTreeBuilder = objectReferences.get(ref);
		if (refTreeBuilder == null) {
			refTreeBuilder = new ObjectReferenceTreeBuilder(ref);
			objectReferences.put(ref, refTreeBuilder);
		}
		refTreeBuilder.addPath(referencePath);
	}
	
	private List<ObjectReference> createPath(final ObjectReference objectReference) {
		List<ObjectReference> result = new ArrayList<ObjectReference>(1);
		result.add(objectReference);
		return result;
	}
	
	private List<ObjectReference> extendPath(final List<ObjectReference> referencePath,
			final ObjectReference objectReference) {
		List<ObjectReference> result = new ArrayList<ObjectReference>(referencePath.size() + 1);
		result.addAll(referencePath);
		result.add(objectReference);
		return result;
	}
	
	/**
	 * Add all objects referenced directly by this use case to the object repository.
	 * 
	 * @return the reference path for the passed use case to use as base path for belonging scenarios etc.
	 */
	public List<ObjectReference> addReferencedUseCaseObjects(final UseCase useCase) {
		List<ObjectReference> referencePath = createPath(createObjectReference("usecase", useCase.getName(),
				useCase.getLabels()));
		addObjects(referencePath, useCase.getDetails());
		addLabels(referencePath, useCase.getLabels());
		return referencePath;
	}
	
	public List<ObjectReference> addReferencedScenarioObjects(List<ObjectReference> referencePath,
			final Scenario scenario) {
		referencePath = extendPath(referencePath,
				createObjectReference("scenario", scenario.getName(), scenario.getLabels()));
		addObjects(referencePath, scenario.getDetails());
		addLabels(referencePath, scenario.getLabels());
		return referencePath;
	}
	
	public void addPageAndStep(List<ObjectReference> referencePath, final Step step, final StepLink stepLink) {
		ObjectReference stepReference = createObjectReference("step", stepLink.getStepIdentifierForObjectRepository(),
				step.getStepDescription().getLabels());
		referencePath = extendPath(referencePath, stepReference);
		addObjects(referencePath, step.getStepDescription().getDetails());
		addObjects(referencePath, step.getMetadata().getDetails());
		addScreenAnnotationObjects(referencePath, step.getScreenAnnotations());
		addPage(referencePath, step.getPage());
		addLabels(referencePath, step.getStepDescription().getLabels());
	}
	
	private void addScreenAnnotationObjects(final List<ObjectReference> referencePath,
			final List<ScreenAnnotation> screenAnnotations) {
		for (ScreenAnnotation screenAnnotation : screenAnnotations) {
			addObjects(referencePath, screenAnnotation.getDetails());
		}
	}

	/**
	 * Add description of a page and all referenced objects
	 */
	private void addPage(final List<ObjectReference> referencePath, final Page page) {
		if (page == null) {
			return;
		}
		
		// Add page description as an object too
		ObjectDescription pageDescription = new ObjectDescription("page", page.getName());
		pageDescription.setDetails(page.getDetails());
		addObject(referencePath, pageDescription, page.getLabels());
	}
	
	/**
	 * Add labels also as objects to the repository
	 */
	private void addLabels(final List<ObjectReference> referencePath, final Labels labels) {
		for (String label : labels.getLabels()) {
			// Save label description (if not yet)
			ObjectDescription labelDescription = new ObjectDescription("label", label);
			addObject(referencePath, labelDescription);
		}
	}
	
	public void calculateAndSaveObjectLists() {
		for (String type : objectTypes) {
			LOGGER.info("    Writing object list for type '" + type + "' ...");
			ObjectList<ObjectDescription> objectsList = new ObjectList<ObjectDescription>();
			List<File> objectFiles = dao.getFiles().getObjectFiles(buildIdentifier, type);
			for (File file : objectFiles) {
				ObjectDescription object = dao.loadObjectDescription(file);
				objectsList.add(object);
			}
			dao.saveObjectsList(buildIdentifier, type, objectsList);
			LOGGER.info("    Finished successfully witing object list for type: " + type);
		}
	}
	
	public void saveCustomObjectTabTrees() {
		customObjectTabsAggregator.saveAggregatedTreeStructures();
	}
	
	public void updateAndSaveObjectIndexesForCurrentCase() {
		LOGGER.info("      Writing object repository index files for last use case. This might take a while ...");
		for (Entry<ObjectReference, ObjectReferenceTreeBuilder> objectRefTreeBuilder : objectReferences.entrySet()) {
			ObjectReference objectRef = objectRefTreeBuilder.getKey();
			ObjectReferenceTreeBuilder referenceTreeBuilder = objectRefTreeBuilder.getValue();
			if (dao.isObjectDescriptionSaved(buildIdentifier, objectRef.getType(), objectRef.getName())) {
				ObjectIndex index = dao.loadObjectIndexIfExistant(buildIdentifier, objectRef.getType(),
						objectRef.getName());
				if (index == null) {
					ObjectDescription object = dao
							.loadObjectDescription(buildIdentifier, objectRefTreeBuilder.getKey());
					ObjectIndex objectIndex = new ObjectIndex();
					objectIndex.setObject(object);
					ObjectTreeNode<ObjectReference> referenceTree = referenceTreeBuilder.build();
					objectIndex.setReferenceTree(referenceTree);
					dao.saveObjectIndex(buildIdentifier, objectIndex);
				} else {
					index.getReferenceTree().addChildren(referenceTreeBuilder.build().getChildren());
					dao.saveObjectIndex(buildIdentifier, index);
				}
			}
		}
		objectReferences.clear();
		LOGGER.info("      Writing object repository index files for last use case finished (success).");
	}
	
	public void removeAnyExistingObjectData() {
		deleteDirectory(dao.getFiles().getObjectsDirectory(buildIdentifier));
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
