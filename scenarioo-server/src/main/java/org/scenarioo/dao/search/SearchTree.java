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

package org.scenarioo.dao.search;

import org.scenarioo.dao.search.dao.*;
import org.scenarioo.model.docu.entities.generic.ObjectReference;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

import java.util.Collections;
import java.util.List;

public class SearchTree {
    private List<SearchDao> searchResults;
    private String q;

	public static SearchTree empty() {
		return new SearchTree(Collections.<SearchDao>emptyList(), "");
	}

    public SearchTree(List<SearchDao> searchResults, String q) {
        this.searchResults = searchResults;
        this.q = q;
    }

    public ObjectTreeNode<ObjectReference> buildObjectTree() {
        ObjectTreeNode<ObjectReference> rootNode = new ObjectTreeNode<ObjectReference>(new ObjectReference("search", q));
        for (SearchDao entry : searchResults) {
			addNode(rootNode, entry);
		}
        return rootNode;
    }

    private void addNode(ObjectTreeNode<ObjectReference> rootNode, SearchDao entry) {
		if (entry instanceof UseCaseSearchDao) {
			putUseCase(rootNode, (UseCaseSearchDao) entry);

		} else if (entry instanceof ScenarioSearchDao) {
			putScenario(rootNode, (ScenarioSearchDao) entry);

		} else if (entry instanceof PageSearchDao) {
			putPage(rootNode, (PageSearchDao) entry);

		} else if (entry instanceof StepSearchDao) {
			putStep(rootNode, (StepSearchDao) entry);
		}
	}

    private ObjectTreeNode<ObjectReference> findChild(String entry, ObjectTreeNode<ObjectReference> useCaseNode) {
		for (ObjectTreeNode<ObjectReference> scenario : useCaseNode.<ObjectReference>getChildren()) {
			if (scenario.getItem().getName().equals(entry)) {
				return scenario;
			}
		}

		return null;
	}

    private ObjectTreeNode<ObjectReference> getOrAddNode(ObjectTreeNode<ObjectReference> parentNode, String entry, String type) {
		ObjectTreeNode<ObjectReference> entryNode = findChild(entry, parentNode);

		if (entryNode != null) {
			return entryNode;
		}

		ObjectTreeNode<ObjectReference> scenarioNode = new ObjectTreeNode<ObjectReference>(new ObjectReference(type, entry));
		parentNode.addChild(scenarioNode);

		return scenarioNode;
	}

    private ObjectTreeNode<ObjectReference> getOrAddStep(ObjectTreeNode<ObjectReference> pageNode, String name) {
		return getOrAddNode(pageNode, name, "step");
	}

    private ObjectTreeNode<ObjectReference> getOrAddPage(ObjectTreeNode<ObjectReference> scenarioNode, String page) {
		return getOrAddNode(scenarioNode, page, "page");
	}

    private ObjectTreeNode<ObjectReference> getOrAddScenario(ObjectTreeNode<ObjectReference> useCaseNode, String scenario) {
		return getOrAddNode(useCaseNode, scenario, "scenario");
	}

    private ObjectTreeNode<ObjectReference> getOrAddUseCase(ObjectTreeNode<ObjectReference> root, String usecase) {
		return getOrAddNode(root, usecase, "usecase");
	}

    private ObjectTreeNode<ObjectReference> putStep(ObjectTreeNode<ObjectReference> root, StepSearchDao entry) {
		ObjectTreeNode<ObjectReference> useCaseNode = getOrAddUseCase(root, entry.get_meta().getUsecase());
		ObjectTreeNode<ObjectReference> scenarioNode = getOrAddScenario(useCaseNode, entry.get_meta().getScenario());
		ObjectTreeNode<ObjectReference> pageNode = getOrAddPage(scenarioNode, entry.get_meta().getPage());

		return getOrAddStep(pageNode, entry.getStep().getStepDescription().getTitle());
	}

    private ObjectTreeNode<ObjectReference> putPage(ObjectTreeNode<ObjectReference> root, PageSearchDao entry) {
		ObjectTreeNode<ObjectReference> useCaseNode = getOrAddUseCase(root, entry.get_meta().getUsecase());
		ObjectTreeNode<ObjectReference> scenarioNode = getOrAddScenario(useCaseNode, entry.get_meta().getScenario());

		return getOrAddPage(scenarioNode, entry.getPage().getName());
	}

    private ObjectTreeNode<ObjectReference> putScenario(ObjectTreeNode<ObjectReference> root, ScenarioSearchDao entry) {
		ObjectTreeNode<ObjectReference> useCaseNode = getOrAddUseCase(root, entry.get_meta().getUsecase());

		return getOrAddScenario(useCaseNode, entry.getScenario().getName());
	}

    private ObjectTreeNode<ObjectReference> putUseCase(ObjectTreeNode<ObjectReference> rootNode, UseCaseSearchDao entry) {
		return getOrAddUseCase(rootNode, entry.getUseCase().getName());
	}
}
