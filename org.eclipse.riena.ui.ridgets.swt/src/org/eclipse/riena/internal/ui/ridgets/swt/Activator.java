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
package org.eclipse.riena.internal.ui.ridgets.swt;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.riena.ui.swt.AbstractRienaUIPlugin;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractRienaUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.eclipse.riena.ui.ridgets.swt"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	// Helper class for shared images. We don't use the plugin's image
	// registry, because this must work in SWT standalone mode as well.
	private static ImageRegistry sharedImages;

	// Helper class for shared colors
	private static SharedColors sharedColors;

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public synchronized void stop(BundleContext context) throws Exception {
		if (sharedColors != null) {
			sharedColors.dispose();
			sharedColors = null;
		}
		if (sharedImages != null) {
			sharedImages.dispose();
			sharedImages = null;
		}
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Return a "shared" image instance using the given colorKey. Shared images
	 * are managed automatically and must not be disposed by client code.
	 * 
	 * @param imageKey
	 *            a non-null String; see {@link SharedImages} for valid keys
	 * @return a non-null Image instance
	 */
	public static synchronized Image getSharedImage(final String imageKey) {
		if (imageKey == null) {
			return null;
		}
		if (sharedImages == null) {
			sharedImages = new ImageRegistry();
			SharedImages.initializeImageRegistry(sharedImages);
		}
		return sharedImages.get(imageKey);
	}

	/**
	 * Return a "shared" color instance using the given colorKey. Shared colors
	 * are managed automatically and must not be disposed by client code.
	 * <p>
	 * Example:
	 * 
	 * <pre>
	 * Color color = Activator.getSharedColor(display, SharedColors.COLOR_MANDATORY);
	 * control.setBackground(color);
	 * </pre>
	 * 
	 * @param display
	 *            a non-null display on which to allocate the color
	 * @param colorKey
	 *            a non-null String; see {@link SharedColors} for valid keys
	 * @return a non-null Color instance
	 */
	public static synchronized Color getSharedColor(Display display, String colorKey) {
		Assert.isNotNull(display);
		if (sharedColors == null) {
			sharedColors = new SharedColors(display);
		}
		return sharedColors.getSharedColor(colorKey);
	}

}
