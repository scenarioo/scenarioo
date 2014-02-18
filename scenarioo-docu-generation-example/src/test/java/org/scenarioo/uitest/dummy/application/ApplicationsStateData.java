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

package org.scenarioo.uitest.dummy.application;

import org.scenarioo.model.docu.entities.generic.ObjectDescription;
import org.scenarioo.model.docu.entities.generic.ObjectTreeNode;

/**
 * Just some example data about internal state of your application that you
 * could collect in your UI tests to store and display it in the documentation.
 * 
 * The collection of such application specific information is very specific for
 * your application.
 * 
 * Some possibilities on how to make such information available in your UI
 * tests:
 * <ul>
 * <li>read it from log files of your application</li>
 * <li>render a special footer on each page of your application with internal
 * application state information (only when testing), this might also be helpful
 * for other debugging/testing purposes.</li>
 * </ul>
 */

public class ApplicationsStateData {

	/**
	 * The name of the current screen/page/view (however you name it in your
	 * application).
	 */
	private String pageName;

	/**
	 * Information about most important code parts and services called between
	 * last step and current step. The leaves of this trees are calls to other
	 * systems (like webservices, databases, etc.)
	 */
	private ObjectTreeNode<ObjectDescription> callTree;

	/**
	 * Probably you are simulating or mocking some backend systems or services
	 * when testing for docu generation. The configuration gives information
	 * about current simulation configuration properties.
	 */
	private ObjectDescription currentSimulationConfiguration;

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public ObjectTreeNode<ObjectDescription> getCallTree() {
		return callTree;
	}

	public void setCallTree(ObjectTreeNode<ObjectDescription> callTree) {
		this.callTree = callTree;
	}

	public ObjectDescription getCurrentSimulationConfiguration() {
		return currentSimulationConfiguration;
	}

	public void setCurrentSimulationConfiguration(
			ObjectDescription currentSimulationConfiguration) {
		this.currentSimulationConfiguration = currentSimulationConfiguration;
	}

}
