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
package org.eclipse.riena.security.common.authorization;

import java.security.CodeSource;
import java.security.Permission;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.Policy;
import java.security.Principal;
import java.security.ProtectionDomain;
import java.util.Enumeration;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.internal.security.common.Activator;
import org.osgi.service.log.LogService;

import sun.security.provider.PolicyFile;

/**
 * 
 */
public class RienaPolicy extends Policy {

	private static Policy defaultPolicy;
	private IPermissionCache permCache;
	private static final Logger LOGGER = Activator.getDefault().getLogger(RienaPolicy.class);

	public RienaPolicy() {
		super();
		Inject.service(IPermissionCache.class.getName()).useRanking().into(this).andStart(
				Activator.getDefault().getContext());
	}

	public void bind(IPermissionCache permCache) {
		this.permCache = permCache;
	}

	public void unbind(IPermissionCache permCache) {
		if (permCache == this.permCache) {
			this.permCache = null;
		}
	}

	/**
	 * 
	 */
	public static void init() {
		RienaPolicy rp = new RienaPolicy();
		Policy.setPolicy(rp);
		defaultPolicy = new PolicyFile();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Policy#getPermissions(java.security.CodeSource)
	 */
	@Override
	public PermissionCollection getPermissions(CodeSource codesource) {
		LOGGER.log(LogService.LOG_DEBUG, "rienapolicy: codesource: getPermissions codesource=" //$NON-NLS-1$
				+ codesource.getLocation());
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.security.Policy#refresh()
	 */
	@Override
	public void refresh() {
		LOGGER.log(LogService.LOG_DEBUG, "rienapolicy: refresh"); //$NON-NLS-1$
	}

	@Override
	public PermissionCollection getPermissions(ProtectionDomain domain) {
		LOGGER.log(LogService.LOG_DEBUG, "rienapolicy: domain: getPermissions domain=" //$NON-NLS-1$
				+ domain.getCodeSource().getLocation());
		return super.getPermissions(domain);
	}

	@Override
	public boolean implies(ProtectionDomain domain, Permission permission) {
		// System.out.print("(Y)");
		if (/* permission instanceof AuthPermission && */domain.getCodeSource().getLocation().toString().contains(
				"/org.eclipse.riena.security.common/")) { //$NON-NLS-1$
			return true;
		}

		// this branch is entered if there is no principal set
		if (domain.getPrincipals() == null || domain.getPrincipals().length == 0) {
			boolean result = defaultPolicy.implies(domain, permission);
			if (!result) {
				LOGGER.log(LogService.LOG_WARNING, "no right to do " + permission + " for " //$NON-NLS-1$ //$NON-NLS-2$
						+ domain.getCodeSource().getLocation() + " no principal"); //$NON-NLS-1$
			}
			return result;
		}

		// this branch is entered if there is at least one principal
		LOGGER.log(LogService.LOG_DEBUG, "rienapolicy: implies "); //$NON-NLS-1$
		for (Principal p : domain.getPrincipals()) {
			LOGGER.log(LogService.LOG_DEBUG, p.toString());
		}
		LOGGER.log(LogService.LOG_DEBUG, " " + permission); //$NON-NLS-1$
		boolean result;
		if (permCache == null) {
			result = defaultPolicy.implies(domain, permission);
		} else {
			Permissions perms = new Permissions();
			for (Principal principal : domain.getPrincipals()) {
				Permissions permsForOnePrincipal = permCache.getPermissions(principal);
				Enumeration<Permission> enumPerms = permsForOnePrincipal.elements();
				while (enumPerms.hasMoreElements()) {
					Permission perm = enumPerms.nextElement();
					perms.add(perm);
				}

			}

			if (perms != null) {
				result = perms.implies(permission);
			} else {
				result = false;
			}
		}
		if (!result) {
			LOGGER.log(LogService.LOG_ERROR, "no right to do " + permission + " for " //$NON-NLS-1$ //$NON-NLS-2$
					+ domain.getCodeSource().getLocation() + " with principal"); //$NON-NLS-1$
		}
		return result;
	}
}
