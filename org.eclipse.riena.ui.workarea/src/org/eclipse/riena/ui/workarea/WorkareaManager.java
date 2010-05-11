/*******************************************************************************
 * Copyright (c) 2007, 2009 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.workarea;

import org.eclipse.riena.internal.ui.workarea.registry.WorkareaDefinitionRegistryFacade;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.ui.ridgets.controller.IController;

public final class WorkareaManager {

	static private WorkareaManager instance = new WorkareaManager();

	private WorkareaDefinitionRegistryFacade registry;

	static public WorkareaManager getInstance() {
		return instance;
	}

	private WorkareaManager() {
		registry = WorkareaDefinitionRegistryFacade.getInstance();
	}

	public IWorkareaDefinition getDefinition(Object key) {
		return registry.getDefinition(key);
	}

	public IWorkareaDefinition registerDefinition(INavigationNode<?> node, Object viewId) {
		return registerDefinition(node, null, viewId, false);
	}

	public IWorkareaDefinition registerDefinition(String id, Object viewId) {
		return registerDefinition(id, null, viewId, false);
	}

	public IWorkareaDefinition registerDefinition(INavigationNode<?> node, Object viewId, boolean isViewShared) {
		return registerDefinition(node, null, viewId, isViewShared);
	}

	public IWorkareaDefinition registerDefinition(String id, Object viewId, boolean isViewShared) {
		return registerDefinition(id, null, viewId, isViewShared);
	}

	public IWorkareaDefinition registerDefinition(INavigationNode<?> node,
			Class<? extends IController> controllerClass, Object viewId) {
		return registerDefinition(node, controllerClass, viewId, false);
	}

	public IWorkareaDefinition registerDefinition(String id, Class<? extends IController> controllerClass, Object viewId) {
		return registerDefinition(id, controllerClass, viewId, false);
	}

	public IWorkareaDefinition registerDefinition(INavigationNode<?> node,
			Class<? extends IController> controllerClass, Object viewId, boolean isViewShared) {
		return registry.registerDefinition(node, controllerClass, viewId, isViewShared);
	}

	public IWorkareaDefinition registerDefinition(String id, Class<? extends IController> controllerClass,
			Object viewId, boolean isViewShared) {
		return registry.registerDefinition(id, controllerClass, viewId, isViewShared);
	}
}