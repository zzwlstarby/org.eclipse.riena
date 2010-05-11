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
package org.eclipse.riena.ui.ridgets.validation;

import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.ui.ridgets.validation.MaxLength;
import org.eclipse.riena.ui.ridgets.validation.MaxLengthNonBlocking;
import org.eclipse.riena.ui.ridgets.validation.ValidationRuleStatus;

/**
 * Tests for the {@link MaxLengthNonBlocking} rule.
 */
@NonUITestCase
public class MaxLengthNonBlockingTest extends MaxLengthTest {

	@Override
	protected MaxLength createRule() {
		return new MaxLengthNonBlocking();
	}

	@Override
	protected MaxLength createRule(int length) {
		return new MaxLengthNonBlocking(length);
	}

	@Override
	public void testIsBlocking() {
		MaxLength rule = createRule(3);
		IStatus status = rule.validate("abcd");

		assertFalse(status.isOK());
		assertEquals(ValidationRuleStatus.ERROR_ALLOW_WITH_MESSAGE, status.getCode());
	}

}