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
package org.eclipse.riena.internal.security.common;

import org.osgi.framework.BundleContext;

import org.eclipse.riena.communication.core.hooks.ICallHook;
import org.eclipse.riena.core.RienaConstants;
import org.eclipse.riena.core.RienaPlugin;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.ContainerModel;
import org.eclipse.riena.core.wire.Wire;
import org.eclipse.riena.internal.security.common.authorization.PermissionCache;
import org.eclipse.riena.internal.security.common.session.SimpleSessionHolder;
import org.eclipse.riena.internal.security.common.session.SimpleThreadedSessionHolder;
import org.eclipse.riena.security.common.ISubjectHolder;
import org.eclipse.riena.security.common.authorization.IAuthorizationService;
import org.eclipse.riena.security.common.authorization.IPermissionCache;
import org.eclipse.riena.security.common.authorization.ISentinelService;
import org.eclipse.riena.security.common.session.ISessionHolder;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends RienaPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.security.common"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		Activator.plugin = this;

		context.registerService(ISessionHolder.class.getName(), ContainerModel.isClient() ? new SimpleSessionHolder()
				: new SimpleThreadedSessionHolder(), null);

		ICallHook hook = new SecurityCallHook();
		Wire.instance(hook).andStart(context);
		context.registerService(ICallHook.class.getName(), hook, null);

		context.registerService(ISubjectHolder.class.getName(), ContainerModel.isClient() ? new SimpleSubjectHolder()
				: new SimpleThreadedSubjectHolder(), null);

		context.registerService(IPermissionCache.class.getName(), new PermissionCache(), null);

		createSentinelServiceAndInjectors();
	}

	private void createSentinelServiceAndInjectors() {
		ISentinelService sentinelService = new SentinelServiceImpl();
		getContext().registerService(ISentinelService.class.getName(), sentinelService,
				RienaConstants.newDefaultServiceProperties());

		Inject.service(IPermissionCache.class).useRanking().into(sentinelService).andStart(
				Activator.getDefault().getContext());
		Inject.service(ISubjectHolder.class).useRanking().into(sentinelService).andStart(
				Activator.getDefault().getContext());
		Inject.service(IAuthorizationService.class).useRanking().into(sentinelService).andStart(
				Activator.getDefault().getContext());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		Activator.plugin = null;
		super.stop(context);
	}

	/**
	 * Get the plugin instance.
	 * 
	 * @return
	 */
	public static Activator getDefault() {
		return plugin;
	}
}