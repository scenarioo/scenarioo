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
 * Just some example UI Action classes of your application (might be Struts actions for examples) to use in dummy
 * example documentation data.
 */
public enum Action {
	
	START_INIT("example.action.StartInitAction", "Initializer for wiki, creates user session and initializes the menu"),
	SEARCH_INIT("example.action.SearchInitAction", "Initializer to init the search page"),
	SEARCH_PROCESS("example.action.SearchProcessAction", "Process user input on search page and start searching"),
	PAGE_SHOW_CONTENT_INIT("example.action.ShowPageInitAction", "Initialize page content to display"),
	PAGE_EDIT_INIT("example.action.EditPageInitAction", "Initialize edit page to edit the page content"),
	PAGE_EDIT_PROCESS("example.action.EditPageProcessAction",
			"Process the input of the user on edit page and store the updated page"),
	AMBIGUOUS_LIST_INIT("example.action.AmbiguousListInitAction",
			"Initilize the list of ambiguities to display to user");
	
	private String name;
	
	private String description;
	
	private Action(final String name, final String description) {
		this.name = name;
		this.description = description;
	}
	
	public String getName() {
		return name;
	}
	
	public String getType() {
		return "uiAction";
	}
	
	public ObjectReference getReference() {
		return new ObjectReference(getType(), getName());
	}
	
	public ObjectDescription getObject() {
		ObjectDescription result = new ObjectDescription(getType(), getName());
		result.addDetail("description", description);
		return result;
	}
	
}
