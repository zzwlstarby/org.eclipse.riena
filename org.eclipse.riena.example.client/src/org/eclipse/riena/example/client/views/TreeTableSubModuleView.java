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
package org.eclipse.riena.example.client.views;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;

import org.eclipse.riena.navigation.ui.swt.views.SubModuleView;
import org.eclipse.riena.ui.ridgets.ITreeRidget;
import org.eclipse.riena.ui.swt.lnf.LnfKeyConstants;
import org.eclipse.riena.ui.swt.lnf.LnfManager;
import org.eclipse.riena.ui.swt.utils.UIControlsFactory;

/**
 * SWT {@link ITreeRidget} sample.
 */
public class TreeTableSubModuleView extends SubModuleView {

	public static final String ID = TreeTableSubModuleView.class.getName();

	@Override
	protected void basicCreatePartControl(Composite parent) {
		parent.setBackground(LnfManager.getLnf().getColor(LnfKeyConstants.SUB_MODULE_BACKGROUND));
		parent.setLayout(new GridLayout(1, false));

		Group group1 = createTreeTableGroup(parent);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(group1);
	}

	// helping methods
	// ////////////////

	private Group createTreeTableGroup(Composite parent) {
		Group group = UIControlsFactory.createGroup(parent, "&Tree Table:"); //$NON-NLS-1$
		GridLayoutFactory.fillDefaults().margins(20, 20).numColumns(1).applyTo(group);

		Composite treeComposite = new Composite(group, SWT.NONE);
		GridDataFactory.fillDefaults().grab(true, true).applyTo(treeComposite);

		Tree tree = UIControlsFactory.createTree(treeComposite, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
		tree.setHeaderVisible(true);
		addUIControl(tree, "tree"); //$NON-NLS-1$

		TreeColumn columnWord = new TreeColumn(tree, SWT.LEFT);
		TreeColumn columnUppercase = new TreeColumn(tree, SWT.LEFT);
		TreeColumn columnACount = new TreeColumn(tree, SWT.LEFT);

		TreeColumnLayout layout = new TreeColumnLayout();
		layout.setColumnData(columnWord, new ColumnWeightData(30));
		layout.setColumnData(columnUppercase, new ColumnWeightData(30));
		layout.setColumnData(columnACount, new ColumnWeightData(30));
		treeComposite.setLayout(layout);

		Composite buttonComposite = createButtonComposite(group);
		GridDataFactory.fillDefaults().grab(true, false).applyTo(buttonComposite);

		return group;
	}

	private Composite createButtonComposite(Group group) {
		Composite buttonComposite = UIControlsFactory.createComposite(group);
		GridLayoutFactory.fillDefaults().numColumns(6).equalWidth(false).applyTo(buttonComposite);

		Button buttonEnableGrouping = UIControlsFactory.createButtonCheck(buttonComposite);
		GridDataFactory.fillDefaults().span(6, 1).grab(true, false).align(SWT.END, SWT.BEGINNING).applyTo(
				buttonEnableGrouping);
		addUIControl(buttonEnableGrouping, "buttonEnableGrouping"); //$NON-NLS-1$

		Button buttonAddSibling = UIControlsFactory.createButton(buttonComposite);
		GridDataFactory.fillDefaults().grab(true, false).align(SWT.END, SWT.BEGINNING).applyTo(buttonAddSibling);
		addUIControl(buttonAddSibling, "buttonAddSibling"); //$NON-NLS-1$

		Button buttonAddChild = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonAddChild, "buttonAddChild"); //$NON-NLS-1$

		Button buttonRename = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonRename, "buttonRename"); //$NON-NLS-1$

		Button buttonDelete = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonDelete, "buttonDelete"); //$NON-NLS-1$

		Button buttonExpand = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonExpand, "buttonExpand"); //$NON-NLS-1$

		Button buttonCollapse = UIControlsFactory.createButton(buttonComposite);
		addUIControl(buttonCollapse, "buttonCollapse"); //$NON-NLS-1$

		return buttonComposite;
	}
}