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
package org.eclipse.riena.sample.snippets;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.ITextRidget;
import org.eclipse.riena.ui.ridgets.controller.AbstractWindowController;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.swt.views.DialogView;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * Snippet using a {@link DialogView}.
 */
public final class SnippetDialogView001 {

	private SnippetDialogView001() {
		// "utility class"
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			GridLayoutFactory.fillDefaults().applyTo(shell);

			Button button = new Button(shell, SWT.BORDER);
			GridDataFactory.fillDefaults().grab(true, false).applyTo(button);

			IActionRidget actionRidget = (IActionRidget) SwtRidgetFactory.createRidget(button);
			actionRidget.setText("Open dialog"); //$NON-NLS-1$
			actionRidget.addListener(new IActionListener() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
				 */
				public void callback() {
					new HelloDialogView(shell).build();
				}
			});

			shell.pack();
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
		} finally {
			display.dispose();
		}
	}

	/**
	 * The controller for the hello dialog of the dialog example.
	 */
	private static class HelloDialogController extends AbstractWindowController {

		public static final String RIDGET_ID_INPUT = "input"; //$NON-NLS-1$
		public static final String RIDGET_ID_OK = "okButton"; //$NON-NLS-1$
		public static final String RIDGET_ID_CANCEL = "cancelButton"; //$NON-NLS-1$

		/*
		 * (non-Javadoc)
		 * 
		 * @seeorg.eclipse.riena.ui.ridgets.controller.AbstractWindowController#
		 * configureRidgets()
		 */
		@Override
		public void configureRidgets() {

			super.configureRidgets();

			getWindowRidget().setTitle("Hello Dialog"); //$NON-NLS-1$

			ITextRidget input = (ITextRidget) getRidget(RIDGET_ID_INPUT);
			input.setText("Input please"); //$NON-NLS-1$

			IActionRidget okAction = (IActionRidget) getRidget(RIDGET_ID_OK);
			okAction.addListener(new IActionListener() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
				 */
				public void callback() {
					getWindowRidget().dispose();
				}
			});
			IActionRidget cancelAction = (IActionRidget) getRidget(RIDGET_ID_CANCEL);
			cancelAction.addListener(new IActionListener() {
				/*
				 * (non-Javadoc)
				 * 
				 * @see org.eclipse.riena.ui.ridgets.IActionListener#callback()
				 */
				public void callback() {
					getWindowRidget().dispose();
				}
			});
		}
	}

	/**
	 * The view for the hello dialog of the dialog example.
	 */
	private static class HelloDialogView extends DialogView {

		public HelloDialogView(Shell shell) {
			super(shell);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.riena.navigation.ui.swt.views.DialogView#createController
		 * ()
		 */
		@Override
		protected AbstractWindowController createController() {
			return new HelloDialogController();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.riena.navigation.ui.swt.views.DialogView#buildView()
		 */
		@Override
		protected Control buildView(Composite parent) {

			super.buildView(parent);

			Composite composite = new Composite(parent, SWT.NONE);
			composite.setLayout(new GridLayout(2, false));

			UIControlsFactory.createLabel(composite, "Input"); //$NON-NLS-1$
			Text input = UIControlsFactory.createText(composite);
			addUIControl(input, HelloDialogController.RIDGET_ID_INPUT);

			Button okButton = UIControlsFactory.createButton(composite);
			okButton.setText("Ok"); //$NON-NLS-1$
			addUIControl(okButton, HelloDialogController.RIDGET_ID_OK);

			Button cancelButton = UIControlsFactory.createButton(composite);
			cancelButton.setText("Cancel"); //$NON-NLS-1$
			addUIControl(cancelButton, HelloDialogController.RIDGET_ID_CANCEL);

			return composite;
		}
	}
}
