/*******************************************************************************
 * Copyright (c) 2007, 2011 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.injector.extension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a <i>creator</i> method so that the returned instance is <b>not</b>
 * <i>wired</i>, i.e. the {@code Wire} has been performed on the created
 * executable extension object.<br>
 * <b>Note: </b>Wiring is the default!
 * 
 * <pre>
 * 
 * &#064;WireExecutable()
 * ISomething createSomething();
 * 
 * </pre>
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DoNotWireExecutable {

}
