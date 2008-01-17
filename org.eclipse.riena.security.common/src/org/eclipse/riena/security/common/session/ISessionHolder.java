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
package org.eclipse.riena.security.common.session;

/**
 * Stores the session and the principal location in the Webservice (axis) context
 * 
 */
public interface ISessionHolder {

    /**
     * Returns the current session
     * 
     * @return current session
     */
    Session getSession();

    /**
     * Sets the current session
     * 
     * @param session
     *            current session.
     */
    void setSession(Session session);

    /**
     * Set JSessionId
     * 
     * @param value
     */
    void setJSessionCookieValue(String value);

    /**
     * Return JSessionId
     * 
     * @return
     */
    String getJSessionCookieValue();
}