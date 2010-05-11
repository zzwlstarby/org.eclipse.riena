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
package org.eclipse.riena.objecttransaction.simple;

import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.objecttransaction.IObjectTransaction;
import org.eclipse.riena.objecttransaction.ObjectTransactionFactory;
import org.eclipse.riena.objecttransaction.simple.value.Addresse;

/**
 * TODO Fehlender Klassen-Kommentar
 */
@NonUITestCase
public class ObjectTransactionDisallowRegisterTest extends RienaTestCase {

	/**
	 * 
	 */
	public void testSimpleAllowRegister() {
		IObjectTransaction objectTransaction = ObjectTransactionFactory.getInstance().createObjectTransaction();
		objectTransaction.allowRegister(false);

		Addresse addresse = new Addresse(true);
		assertTrue("kunde must not be registered", !objectTransaction.isRegistered(addresse));

		objectTransaction.allowRegister(true);
		addresse = new Addresse(true);
		assertTrue("kunde must be registered", objectTransaction.isRegistered(addresse));

		showStatus("testSimpleAllNew", objectTransaction);
	}

	private void showStatus(String testName, IObjectTransaction objectTransaction) {
		System.out.println("testname >>>>>" + testName + "<<<<<");
		System.out.println(objectTransaction);
	}
}