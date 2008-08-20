/*******************************************************************************
 * Copyright (c) 2007, 2008 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.example.client.navigation.model;

import org.eclipse.riena.navigation.IModuleGroupNode;
import org.eclipse.riena.navigation.IModuleNode;
import org.eclipse.riena.navigation.INavigationNodeBuilder;
import org.eclipse.riena.navigation.INavigationNodeId;
import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.NavigationArgument;
import org.eclipse.riena.navigation.model.ModuleGroupNode;
import org.eclipse.riena.navigation.model.ModuleNode;
import org.eclipse.riena.navigation.model.NavigationNodeId;
import org.eclipse.riena.navigation.model.SubModuleNode;

/**
 *
 */
public class TableTextAndTreeNodeBuilder implements INavigationNodeBuilder {

	/**
	 * @see org.eclipse.riena.navigation.INavigationNodeBuilder#buildNode(org.eclipse.riena.navigation.INavigationNodeId,
	 *      org.eclipse.riena.navigation.NavigationArgument)
	 */
	public IModuleGroupNode buildNode(INavigationNodeId presentationId, NavigationArgument navigationArgument) {
		IModuleGroupNode node = new ModuleGroupNode(presentationId);
		IModuleNode module = new ModuleNode(null, "Table,Text&Tree"); //$NON-NLS-1$
		node.addChild(module);
		ISubModuleNode subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.table"), "Table"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.text"), "Text"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		subModule = new SubModuleNode(new NavigationNodeId("org.eclipse.riena.example.tree"), "Tree"); //$NON-NLS-1$ //$NON-NLS-2$
		module.addChild(subModule);
		return node;
	}
}
