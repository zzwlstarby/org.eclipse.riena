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
package org.eclipse.riena.internal.navigation.ui.filter;

import org.eclipse.riena.core.marker.IMarker;
import org.eclipse.riena.core.util.StringMatcher;
import org.eclipse.riena.navigation.INavigationNode;
import org.eclipse.riena.navigation.NavigationNodeUtility;
import org.eclipse.riena.ui.filter.IUIFilterRuleMarkerNavigation;
import org.eclipse.riena.ui.filter.impl.AbstractUIFilterRuleMarker;

/**
 * Filter rule to provide a marker for a node of the navigation.
 */
public abstract class AbstractUIFilterRuleNavigationMarker extends AbstractUIFilterRuleMarker implements
		IUIFilterRuleMarkerNavigation {

	private String nodeIdPattern;

	/**
	 * Creates a new instance of {@code AbstractUIFilterRuleNavigationMarker}.
	 * 
	 * @param nodeIdPattern
	 *            pattern ({@link StringMatcher}) for navigation node IDs
	 * @param marker
	 */
	public AbstractUIFilterRuleNavigationMarker(String nodeIdPattern, IMarker marker) {
		super(marker);
		this.nodeIdPattern = nodeIdPattern;
	}

	public boolean matches(Object... args) {

		if ((args == null) || (args.length <= 0)) {
			return false;
		}

		if (args[0] instanceof INavigationNode<?>) {
			INavigationNode<?> node = (INavigationNode<?>) args[0];
			String longNodeId = NavigationNodeUtility.getNodeLongId(node);
			StringMatcher stringMatcher = new StringMatcher(nodeIdPattern);
			return stringMatcher.match(longNodeId);
		} else {
			return false;
		}

	}

	public void apply(Object object) {
		if (object instanceof INavigationNode<?>) {
			INavigationNode<?> node = (INavigationNode<?>) object;
			node.addMarker(getMarker());
		}

	}

	public void remove(Object object) {
		if (object instanceof INavigationNode<?>) {
			INavigationNode<?> node = (INavigationNode<?>) object;
			node.removeMarker(getMarker());
		}
	}

	public void setNode(String id) {
		this.nodeIdPattern = id;
	}

}