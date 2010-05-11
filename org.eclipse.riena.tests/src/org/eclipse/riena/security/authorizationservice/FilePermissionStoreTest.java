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
package org.eclipse.riena.security.authorizationservice;

import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.security.Permission;
import java.security.Permissions;
import java.util.Enumeration;

import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.security.common.authentication.SimplePrincipal;
import org.eclipse.riena.security.simpleservices.authorizationservice.store.FilePermissionStore;
import org.xml.sax.SAXException;

@NonUITestCase
public class FilePermissionStoreTest extends TestCase {

	public void testReadPermission() throws SAXException, IOException, ParserConfigurationException {
		InputStream inputStream = this.getClass().getResourceAsStream("policy-def-test.xml");
		FilePermissionStore permStore = new FilePermissionStore(inputStream);
		Permissions perms = permStore.loadPermissions(new SimplePrincipal("christian"));
		assertTrue(perms != null);
		Enumeration<Permission> enumPerms = perms.elements();
		Permission p;
		int count = 0;

		while (enumPerms.hasMoreElements()) {
			p = enumPerms.nextElement();
			count++;
			if (p instanceof FilePermission) {
				FilePermission fp = (FilePermission) p;
				assertEquals("*.tmp", fp.getName());
				assertTrue(fp.getActions().equals("read"));
			} else {
				if (p instanceof TestcasePermission) {
					TestcasePermission tcp = (TestcasePermission) p;
					assertEquals("testPerm", tcp.getName());
				} else {
					assertTrue("unknown permission " + p, false);
				}
			}
		}
		assertTrue(count == 2);
	}
}