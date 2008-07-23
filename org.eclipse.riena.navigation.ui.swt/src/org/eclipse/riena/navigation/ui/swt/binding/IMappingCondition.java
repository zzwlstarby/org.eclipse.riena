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
package org.eclipse.riena.navigation.ui.swt.binding;

/**
 * A mapping condition; used by the {@link DefaultSwtControlRidgetMapper}.
 * <p>
 * The {@link #isMatch(Object)} must return true before a widget-based match
 * will be considered. This can be used to inspect the state of the widget
 * before deciding wether a widget-to-ridget mapping applies.
 * 
 * @see DefaultSwtControlRidgetMapper#addMapping(Class, Class,
 *      IMappingCondition)
 * @see TreeWithColumnsCondition
 */
public interface IMappingCondition {
	/**
	 * Returns true if the given widget matches the implemented condition.
	 * 
	 * @param widget
	 *            a widget instance
	 */
	boolean isMatch(Object widget);
}
