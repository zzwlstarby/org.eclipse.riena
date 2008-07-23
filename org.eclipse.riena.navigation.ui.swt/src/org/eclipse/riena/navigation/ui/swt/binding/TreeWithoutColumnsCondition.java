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

import org.eclipse.swt.widgets.Tree;

/**
 * This mapping condition returns true if the given widget is a {@link Tree}
 * with exactly zero columns.
 */
final class TreeWithoutColumnsCondition implements IMappingCondition {

	public boolean isMatch(Object widget) {
		boolean result = false;
		if (widget instanceof Tree) {
			result = ((Tree) widget).getColumnCount() == 0;
		}
		return result;
	}

}
