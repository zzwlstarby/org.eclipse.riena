/*******************************************************************************
 * Copyright (c) 2007, 2010 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.core.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.osgi.service.log.LogService;

import org.eclipse.core.runtime.Assert;
import org.eclipse.equinox.log.Logger;

import org.eclipse.riena.core.Log4r;
import org.eclipse.riena.internal.core.Activator;

/**
 * A collection of i/o utilities.
 */
public final class IOUtils {

	private static final Logger LOGGER = Log4r.getLogger(Activator.getDefault(), IOUtils.class);

	private IOUtils() {
		// utility
	}

	/**
	 * Copy file
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 * @pre from!=null && to!=null
	 */
	public static void copyFile(final File from, final File to) throws IOException {
		Assert.isNotNull(from, "from"); //$NON-NLS-1$
		Assert.isNotNull(to, "to"); //$NON-NLS-1$

		FileInputStream inputStream = null;
		FileOutputStream outputStream = null;
		FileChannel sourceChannel = null;
		FileChannel destinationChannel = null;
		try {
			inputStream = new FileInputStream(from);
			sourceChannel = inputStream.getChannel();

			outputStream = new FileOutputStream(to);
			destinationChannel = outputStream.getChannel();

			sourceChannel.transferTo(0, sourceChannel.size(), destinationChannel);
		} finally {
			IOUtils.close(inputStream);
			IOUtils.close(outputStream);
			IOUtils.close(sourceChannel);
			IOUtils.close(destinationChannel);
		}
	}

	/**
	 * Close the given closeable.
	 * 
	 * @param closeable
	 *            the closeable to close.
	 */
	public static void close(final Closeable closeable) {
		if (closeable == null) {
			return;
		}
		try {
			closeable.close();
		} catch (final IOException e) {
			LOGGER.log(LogService.LOG_DEBUG, "Could not close given closeable.", e); //$NON-NLS-1$
		}
	}

}
