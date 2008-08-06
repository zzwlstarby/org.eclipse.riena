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
package org.eclipse.riena.ui.ridgets;

import org.eclipse.core.databinding.observable.list.IObservableList;

/**
 * A ridget that allows for the selection of one or more options.
 * 
 * @see ISingleChoiceRidget
 * @see IMultipleChoiceRidget
 */
public interface IChoiceRidget extends IMarkableRidget {

	/**
	 * Property name of the selection property.
	 * 
	 * @see SingleChoiceSwingRidget.getSelection()
	 * @see SingleChoiceSwingRidget.setSelection(Object selection)
	 * @see MultipleChoiceSwingRidget.getSelection()
	 * @see MultipleChoiceSwingRidget.setSelection(List selection)
	 */
	String PROPERTY_SELECTION = "selection"; //$NON-NLS-1$

	/**
	 * Return the observable list holding the options.
	 * 
	 * @return the observable list of options.
	 */
	IObservableList getObservableList();
}
