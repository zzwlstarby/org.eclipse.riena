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

import org.eclipse.riena.ui.ridgets.controller.IController;

/**
 * A WorkareaDefinition consists of viewId and a {@link IController}. Also other
 * information for creating a view or a controller of a navigation node are
 * stored.
 */
public class WorkareaDefinition implements IWorkareaDefinition {

	private final Class<? extends IController> controllerClass;
	private final Object viewId;
	private boolean viewShared;
	private boolean requiredPreparation;

	/**
	 * Creates a new instance of {@code WorkareaDefinition} with only a view ID
	 * and the information whether the view is shared.
	 * 
	 * @param viewId
	 *            ID of the view (see <code>org.eclipse.ui.views</code>
	 *            extension point)
	 */
	public WorkareaDefinition(Object viewId) {
		this(null, viewId);
	}

	/**
	 * Creates a new instance of {@code WorkareaDefinition} with controller
	 * class, view ID and the information whether the view is shared.
	 * 
	 * @param controllerClass
	 *            the controller class to be used with the view
	 * @param viewId
	 *            ID of the view (see <code>org.eclipse.ui.views</code>
	 *            extension point)
	 */
	public WorkareaDefinition(Class<? extends IController> controllerClass, Object viewId) {
		this.controllerClass = controllerClass;
		this.viewId = viewId;
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<? extends IController> getControllerClass() {
		return controllerClass;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @return the created controller or {@code null} if no controller class
	 *         exists
	 */
	public IController createController() throws IllegalAccessException, InstantiationException {

		if (getControllerClass() != null) {
			return getControllerClass().newInstance();
		} else {
			return null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public Object getViewId() {
		return viewId;
	}

	/**
	 * {@inheritDoc}
	 */
	public boolean isViewShared() {
		return viewShared;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public boolean isRequiredPreparation() {
		return requiredPreparation;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public void setViewShared(boolean shared) {
		this.viewShared = shared;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @since 2.0
	 */
	public void setRequiredPreparation(boolean required) {
		this.requiredPreparation = required;
	}

}