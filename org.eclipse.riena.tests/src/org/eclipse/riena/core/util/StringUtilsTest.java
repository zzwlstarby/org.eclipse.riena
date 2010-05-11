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
package org.eclipse.riena.core.util;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;

/**
 * Nomen est omen!
 */
@NonUITestCase
public class StringUtilsTest extends TestCase {

	/**
	 * Nomen est omen!
	 */
	public void testIsEmpty() {
		assertTrue(StringUtils.isEmpty(null));
		assertTrue(StringUtils.isEmpty(""));
		assertFalse(StringUtils.isEmpty(" "));
		assertFalse(StringUtils.isEmpty(" a "));
	}

	/**
	 * Nomen est omen!
	 */
	public void testIsGiven() {
		assertFalse(StringUtils.isGiven(null));
		assertFalse(StringUtils.isGiven(""));
		assertTrue(StringUtils.isGiven(" "));
		assertTrue(StringUtils.isGiven(" a "));
	}

	/**
	 * Nomen est omen!
	 */
	public void testIsDeepEmpty() {
		assertTrue(StringUtils.isDeepEmpty(null));
		assertTrue(StringUtils.isDeepEmpty(""));
		assertTrue(StringUtils.isDeepEmpty(" "));
		assertFalse(StringUtils.isDeepEmpty(" a "));
	}

	/**
	 * Nomen est omen!
	 */
	public void testEquals() {
		assertTrue(StringUtils.equals(null, null));

		assertFalse(StringUtils.equals(null, ""));
		assertFalse(StringUtils.equals("", null));

		String c = "c";
		assertTrue(StringUtils.equals(c, c));

		assertTrue(StringUtils.equals("c", "c"));

		assertFalse(StringUtils.equals(null, "a"));
		assertFalse(StringUtils.equals("a", null));

		assertFalse(StringUtils.equals("b", "a"));
		assertFalse(StringUtils.equals("a", "b"));
	}

	/**
	 * Minima maxima sunt!
	 */
	public void testCount() {
		assertEquals(1, StringUtils.count("abb", 'a'));
		assertEquals(1, StringUtils.count("bba", 'a'));
		assertEquals(1, StringUtils.count("bab", 'a'));

		assertEquals(3, StringUtils.count("aaa", 'a'));

		assertEquals(0, StringUtils.count("", 'a'));
		assertEquals(0, StringUtils.count(null, 'a'));
		assertEquals(0, StringUtils.count("bcd", 'a'));
	}

	/**
	 * In experior veritas!
	 */
	public void testCapitalize() {
		assertEquals(null, StringUtils.capitalize(null));
		assertEquals("", StringUtils.capitalize(""));
		assertEquals("!a", StringUtils.capitalize("!a"));

		assertEquals("A", StringUtils.capitalize("A"));
		assertEquals("A", StringUtils.capitalize("a"));
		assertEquals("Veritas", StringUtils.capitalize("veritas"));
	}
}