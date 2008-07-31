/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.navigation.model;

import org.eclipse.riena.navigation.ISubModuleNode;
import org.eclipse.riena.navigation.listener.ISubModuleNodeListener;

/**
 * Default implementation for the sub module node
 */
public class SubModuleNode extends NavigationNode<ISubModuleNode, ISubModuleNode, ISubModuleNodeListener> implements ISubModuleNode {

	/**
	 * 
	 */
	public SubModuleNode() {
		super();
	}

	/**
	 * @param children
	 */
	public SubModuleNode(ISubModuleNode... children) {
		super(children);
	}

	/**
	 * @param label
	 * @param children
	 */
	public SubModuleNode(String label, ISubModuleNode... children) {
		super(label, children);
	}

	/**
	 * @param label
	 */
	public SubModuleNode(String label) {
		super(label);
	}

}
