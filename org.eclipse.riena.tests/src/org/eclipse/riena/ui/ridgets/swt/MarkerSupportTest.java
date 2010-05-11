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
package org.eclipse.riena.ui.ridgets.swt;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.core.util.ReflectionUtils;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.UITestCase;
import org.eclipse.riena.internal.ui.ridgets.swt.LabelRidget;
import org.eclipse.riena.internal.ui.ridgets.swt.TextRidget;
import org.eclipse.riena.ui.core.marker.ErrorMarker;
import org.eclipse.riena.ui.core.marker.MandatoryMarker;
import org.eclipse.riena.ui.core.marker.NegativeMarker;
import org.eclipse.riena.ui.core.marker.OutputMarker;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.lnf.rienadefault.RienaDefaultLnf;
import org.eclipse.riena.ui.swt.utils.SwtUtilities;

/**
 * Test the {@code MarkSupport} class.
 */
@UITestCase
public class MarkerSupportTest extends RienaTestCase {

	private static final String HIDE_DISABLED_RIDGET_CONTENT = "HIDE_DISABLED_RIDGET_CONTENT";

	private Display display;
	private Shell shell;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		display = Display.getDefault();
		shell = new Shell(display);
	}

	@Override
	protected void tearDown() {
		SwtUtilities.disposeWidget(shell);
		display = null;
	}

	public void testHideDisabledRidgetContentSystemProperty() throws IOException {
		System.clearProperty(HIDE_DISABLED_RIDGET_CONTENT);
		assertFalse(getHideDisabledRidgetContent());

		System.setProperty(HIDE_DISABLED_RIDGET_CONTENT, Boolean.FALSE.toString());
		assertFalse(getHideDisabledRidgetContent());

		System.setProperty(HIDE_DISABLED_RIDGET_CONTENT, Boolean.TRUE.toString());
		assertTrue(getHideDisabledRidgetContent());
	}

	/**
	 * Tests the <i>private</i> method {@code createErrorDecoration}.
	 * 
	 * @throws Exception
	 *             handled by JUnit
	 */
	public void testCreateErrorDecoration() throws Exception {
		RienaDefaultLnf originalLnf = LnfManager.getLnf();
		DefaultRealm realm = new DefaultRealm();
		try {
			final Text text = new Text(shell, SWT.NONE);
			ITextRidget ridget = new TextRidget();
			ridget.setUIControl(text);
			MarkerSupport support = new MarkerSupport(ridget, null);

			LnfManager.setLnf(new MyLnf());
			ControlDecoration deco = ReflectionUtils.invokeHidden(support, "createErrorDecoration", text);
			assertEquals(100, deco.getMarginWidth());
			assertNotNull(deco.getImage());

			LnfManager.setLnf(new MyNonsenseLnf());
			deco = ReflectionUtils.invokeHidden(support, "createErrorDecoration", text);
			assertEquals(1, deco.getMarginWidth());
			assertNotNull(deco.getImage());

			support = null;
			SwtUtilities.disposeWidget(text);
		} finally {
			LnfManager.setLnf(originalLnf);
			realm.dispose();
		}
	}

	private static boolean getHideDisabledRidgetContent() throws IOException {
		MarkSupportClassLoader freshLoader = new MarkSupportClassLoader();
		Class<?> markerSupportClass = freshLoader.getFreshMarkSupportClass();
		return ReflectionUtils.getHidden(markerSupportClass, HIDE_DISABLED_RIDGET_CONTENT);
	}

	public void testDisabledMarker() throws Exception {
		DefaultRealm realm = new DefaultRealm();
		try {
			final Label control = new Label(shell, SWT.NONE);

			LabelRidget ridget = new LabelRidget();
			ridget.setUIControl(control);
			BasicMarkerSupport msup = ReflectionUtils.invokeHidden(ridget, "createMarkerSupport");
			Object visualizer = ReflectionUtils.invokeHidden(msup, "getDisabledMarkerVisualizer", (Object[]) null);

			assertNotNull(visualizer);

			ridget.setEnabled(false);

			assertEquals(1, control.getListeners(SWT.Paint).length);

			ridget.setEnabled(true);

			assertEquals(0, control.getListeners(SWT.Paint).length);
		} finally {
			realm.dispose();
		}
	}

	public void testClearAllMarkes() {

		DefaultRealm realm = new DefaultRealm();
		try {

			Text control = new Text(shell, SWT.NONE);
			shell.setVisible(true);
			Color background = control.getBackground();
			Color foreground = control.getForeground();

			ITextRidget ridget = new TextRidget();
			ridget.setUIControl(control);
			MyMarkerSupport markerSupport = new MyMarkerSupport();
			markerSupport.init(ridget, null);

			ridget.addMarker(new MandatoryMarker());
			markerSupport.updateMarkers();
			assertFalse(background.equals(control.getBackground()));

			markerSupport.clearAllMarkers(control);
			assertEquals(background, control.getBackground());

			ridget.addMarker(new OutputMarker());
			markerSupport.updateMarkers();
			assertFalse(background.equals(control.getBackground()));

			markerSupport.clearAllMarkers(control);
			assertEquals(background, control.getBackground());

			ridget.addMarker(new NegativeMarker());
			markerSupport.updateMarkers();
			assertFalse(foreground.equals(control.getForeground()));

			markerSupport.clearAllMarkers(control);
			assertEquals(foreground, control.getForeground());

			ridget.addMarker(new ErrorMarker());
			markerSupport.updateMarkers();
			ControlDecoration errorDecoration = ReflectionUtils.getHidden(markerSupport, "errorDecoration");
			assertNotNull(errorDecoration);
			assertTrue(errorDecoration.isVisible());

			markerSupport.clearAllMarkers(control);
			assertFalse(errorDecoration.isVisible());

		} finally {
			realm.dispose();
		}

	}

	// helping classes
	//////////////////

	/**
	 * This {@code ClassLoader}s method {@code getFreshMarkSupportClass()}
	 * retrieves with each call a new, fresh {@code MarkSupport} class. This
	 * allows testing of the static field which gets initialized on class load.
	 */
	private static class MarkSupportClassLoader extends ClassLoader {
		public MarkSupportClassLoader() {
			super(MarkSupportClassLoader.class.getClassLoader());
		}

		public Class<?> getFreshMarkSupportClass() throws IOException {
			String resource = MarkerSupport.class.getName().replace('.', '/') + ".class";
			URL classURL = MarkerSupportTest.class.getClassLoader().getResource(resource);
			InputStream is = classURL.openStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			int ch = -1;
			while ((ch = is.read()) != -1) {
				baos.write(ch);
			}
			byte[] bytes = baos.toByteArray();
			Class<?> cl = super.defineClass(MarkerSupport.class.getName(), bytes, 0, bytes.length);
			resolveClass(cl);
			return cl;
		}
	}

	/**
	 * Look and Feel with correct setting.
	 */
	private static class MyLnf extends RienaDefaultLnf {
		@Override
		protected void initSettingsDefaults() {
			getSettingTable().put(LnfKeyConstants.ERROR_MARKER_MARGIN, 100);
			getSettingTable().put(LnfKeyConstants.ERROR_MARKER_HORIZONTAL_POSITION, SWT.RIGHT);
			getSettingTable().put(LnfKeyConstants.ERROR_MARKER_VERTICAL_POSITION, SWT.BOTTOM);
		}
	}

	/**
	 * Look and Feel with invalid setting: no settings and no images
	 */
	private static class MyNonsenseLnf extends RienaDefaultLnf {
		@Override
		protected void initSettingsDefaults() {
			getSettingTable().clear();
		}

		@Override
		protected void initImageDefaults() {
			getResourceTable().clear();
		}
	}

	/**
	 * This extension changes the visibility of some protected methods for
	 * testing.
	 */
	private static class MyMarkerSupport extends MarkerSupport {

		@Override
		public void clearAllMarkers(Control control) {
			super.clearAllMarkers(control);
		}

	}

}