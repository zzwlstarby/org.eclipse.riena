/*******************************************************************************
 * Copyright (c) 2007, 2014 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.ui.swt;

import java.util.Hashtable;

import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import org.eclipse.osgi.framework.console.CommandProvider;

import org.eclipse.riena.internal.ui.swt.console.UIControlsStatisticCommandProvider;
import org.eclipse.riena.ui.swt.AbstractRienaUIPlugin;
import org.eclipse.riena.ui.swt.lnf.LnfManager;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractRienaUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.ui.swt"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	private ServiceRegistration controlsStatisticReg;

	/**
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		final UIControlsStatisticCommandProvider statisticCommand = new UIControlsStatisticCommandProvider();
		controlsStatisticReg = context.registerService(CommandProvider.class.getName(), statisticCommand,
				new Hashtable<String, String>());
	}

	/**
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		controlsStatisticReg.unregister();
		controlsStatisticReg = null;
		super.stop(context);
		LnfManager.dispose();
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

}
