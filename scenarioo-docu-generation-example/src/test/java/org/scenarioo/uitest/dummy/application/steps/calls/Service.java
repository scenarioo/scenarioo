/* Copyright (c) 2014, scenarioo.org Development Team
 * All rights reserved.
 *
 * See https://github.com/scenarioo?tab=members
 * for a complete list of contributors to this project.
 *
 * Redistribution and use of the Scenarioo Examples in source and binary forms,
 * with or without modification, are permitted provided that the following
 * conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 *
 * * Redistributions in binary form must reproduce the above copyright notice, this
 *   list of conditions and the following disclaimer in the documentation and/or
 *   other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.scenarioo.uitest.dummy.application.steps.calls;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectReference;

/**
 * Just some example actions to use in dummy example documentation data.
 */
public enum Service {
	
	MENU("MenuService.loadMenu", "MENUSYS:LoadMenuService.load", "Service to load current wiki menu."),
	SEARCH("SearchService.search", "TIBCO:FastSearchService.getSearchResults", "PowerGridSearch.search",
			"Search for pages using the power grid"),
	AMBIGUITIES("SearchService.loadAmbiguitiesList", "TIBCO:AmbiguitiesService.getAmbiguitiesList",
			"ContentDB.loadAmbiguities",
			"Service to load a list of ambiguities to display to the user for a ambiguous search."),
	CONTENT_LOAD("ContentService.loadPage", "TIBCO:PageContentService.getPage", "ContentDB.loadPage",
			"Service to load the whole page content of a page"),
	AUTHENTICATION_CHECK(
			"AuthenticationService.checkUserAuthenticatedServiceOperationWithAVeryVeryVeryUglyAndLongOperationNameJustToCheckHandlingOfLongNames",
			"LDAP:AuthenticationService",
			"Service to check via LDAP the authentication for a user for given operation."),
	CONTENT_SAVE(
			"ContentService.savePage",
			"TIBCO:PageContentService.updatePage", "ContentDB.savePage",
			"Service to save the updated page content as edited by a user");
	
	private String name;
	private String eaiName;
	private String realName;
	private String description;
	
	private Service(final String name, final String realName, final String description) {
		this.name = name;
		this.realName = realName;
		this.eaiName = null;
		this.description = description;
	}
	
	private Service(final String name, final String eaiName, final String realName, final String description) {
		this.name = name;
		this.realName = realName;
		this.eaiName = eaiName;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return "service";
	}
	
	public ObjectReference getReference() {
		return new ObjectReference(getType(), getName());
	}
	
	public ObjectDescription getObject() {
		ObjectDescription result = new ObjectDescription(getType(), getName());
		result.addDetail("description", description);
		result.addDetail("eaiName", eaiName);
		result.addDetail("realName", realName);
		return result;
	}
	
}
