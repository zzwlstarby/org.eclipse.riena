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
package org.eclipse.riena.ui.filter;

/**
 * Interface for a UIFiltere extension that defines how to create a filter with
 * a list of attributes.
 */
public interface IUIFilterExtension {

	/**
	 * Returns the filterID
	 */

	String getFilterId();

	/**
	 * Returns the list of attributes
	 */

	IMarkerAttribute[] getMarkerAttributes();

	/**
	 * Returns the nodeIds
	 */

	IFilterNodeIds[] getNodeIds();
}
