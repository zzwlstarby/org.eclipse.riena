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

import java.util.List;

import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.ridgets.swt.SwtRidgetFactory;
import org.eclipse.riena.ui.ridgets.tree2.TreeNode;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tree;

/**
 * Ties the visibility of a tree item to the value of a method.
 */
public class SnippetTreeRidget002 {

	public SnippetTreeRidget002(Shell shell) {

		Tree tree = new Tree(shell, SWT.FULL_SELECTION | SWT.MULTI);

		final ITreeRidget treeRidget = (ITreeRidget) SwtRidgetFactory.createRidget(tree);
		TreeNode[] roots = createTreeInput();
		treeRidget.bindToModel(roots, TreeNode.class, TreeNode.PROPERTY_CHILDREN, TreeNode.PROPERTY_PARENT,
				TreeNode.PROPERTY_VALUE, null, TreeNode.PROPERTY_VISIBLE);
		//                                     ^^^^^^^^^^^^^^^^^^^^^^^^^
		// This ties the visibility of a tree item to the "boolean isVisible()"
		// method of a TreeNode. You can use any method, as long as it returns
		// a boolean and has no arguments. For example "upperCase" would tie
		// the visibility to "isUpperCase() or getUpperCase()"

		Button button = new Button(shell, SWT.PUSH);
		button.setText("Hide"); //$NON-NLS-1$
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				final List<Object> selection = treeRidget.getSelection();
				for (Object element : selection) {
					((TreeNode) element).setVisible(false);
				}
			};
		});
	}

	public static void main(String[] args) {
		Display display = Display.getDefault();
		try {
			final Shell shell = new Shell();
			shell.setLayout(new FillLayout());
			new SnippetTreeRidget002(shell);
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

	private TreeNode[] createTreeInput() {

		TreeNode root = new TreeNode("Gods"); //$NON-NLS-1$

		TreeNode greek = new TreeNode(root, "Greek Gods"); //$NON-NLS-1$
		new TreeNode(greek, "Aphrodite"); //$NON-NLS-1$
		new TreeNode(greek, "Apollo"); //$NON-NLS-1$
		new TreeNode(greek, "Ares"); //$NON-NLS-1$
		new TreeNode(greek, "Artemis"); //$NON-NLS-1$
		new TreeNode(greek, "Athena"); //$NON-NLS-1$
		new TreeNode(greek, "Demeter"); //$NON-NLS-1$
		new TreeNode(greek, "Dionysus"); //$NON-NLS-1$
		new TreeNode(greek, "Hephaestus"); //$NON-NLS-1$
		new TreeNode(greek, "Hera"); //$NON-NLS-1$
		new TreeNode(greek, "Hermes"); //$NON-NLS-1$
		new TreeNode(greek, "Hestia"); //$NON-NLS-1$
		new TreeNode(greek, "Zeus"); //$NON-NLS-1$
		TreeNode greekDemigods = new TreeNode(greek, "Demigods"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Achilles"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Hercules"); //$NON-NLS-1$
		new TreeNode(greekDemigods, "Perseus "); //$NON-NLS-1$

		TreeNode roman = new TreeNode(root, "Roman Gods"); //$NON-NLS-1$
		new TreeNode(roman, "Diana"); //$NON-NLS-1$
		new TreeNode(roman, "Janus"); //$NON-NLS-1$
		new TreeNode(roman, "Juno"); //$NON-NLS-1$
		new TreeNode(roman, "Jupiter"); //$NON-NLS-1$
		new TreeNode(roman, "Mars"); //$NON-NLS-1$
		new TreeNode(roman, "Saturn"); //$NON-NLS-1$
		new TreeNode(roman, "Vesta"); //$NON-NLS-1$

		TreeNode germanic = new TreeNode(root, "Germanic Gods"); //$NON-NLS-1$
		new TreeNode(germanic, "Thor"); //$NON-NLS-1$
		new TreeNode(germanic, "Odin"); //$NON-NLS-1$
		new TreeNode(germanic, "Tyr"); //$NON-NLS-1$
		new TreeNode(germanic, "Frigg"); //$NON-NLS-1$

		return new TreeNode[] { root };
	}

}