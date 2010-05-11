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
package org.eclipse.riena.navigation.ui.swt.views;

import org.eclipse.riena.navigation.INavigationNode;

/**
 * Interface for listeners of view components, which want to react on changes of
 * a navigation node.
 */
public interface IComponentUpdateListener {

	/**
	 * React on changes done one navigation node <code>node</code>.
	 * 
	 * @param node
	 *            the node for which the update is triggert.
	 */
	void update(INavigationNode<?> node);

}