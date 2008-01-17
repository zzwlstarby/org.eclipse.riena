/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/

package org.eclipse.riena.security.common.authentication;

/**
 * Checked Exception which indicates an non-fatal error situation caused by the authentication module.
 * 
 */
public class AuthenticationException extends Exception {

    /**
     * constructor.
     */
    public AuthenticationException() {
        super();
    }

    /**
     * constructor.
     * 
     * @param message
     *            the detail message
     */
    public AuthenticationException(String message) {
        super(message);
    }
}