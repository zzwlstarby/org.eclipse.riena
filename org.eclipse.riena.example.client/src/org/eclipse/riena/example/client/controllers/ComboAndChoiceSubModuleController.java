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
package org.eclipse.riena.example.client.controllers;

import org.eclipse.riena.beans.common.SingleSelectionListBean;
import org.eclipse.riena.navigation.ui.controllers.SubModuleController;
import org.eclipse.riena.ui.ridgets.IActionListener;
import org.eclipse.riena.ui.ridgets.IActionRidget;
import org.eclipse.riena.ui.ridgets.IComboRidget;
import org.eclipse.riena.ui.ridgets.ISingleChoiceRidget;

/**
 *
 */
public class ComboAndChoiceSubModuleController extends SubModuleController {

	private IComboRidget comboRidgetWithModel;
	private IComboRidget comboRidgetWithoutModel;
	private IActionRidget updateAllRidgetsFromModel;
	private IActionRidget bindComboToModel;
	private IActionRidget bindChoiceToModel;

	@Override
	public void configureRidgets() {
		comboRidgetWithModel = (IComboRidget) getRidget("comboBoxWithModel"); //$NON-NLS-1$
		SingleSelectionListBean colors = new SingleSelectionListBean(new Object[] {
				"white", "black", "red", "blue", "green", "brown", "yellow" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
		colors.setSelection("blue"); //$NON-NLS-1$
		comboRidgetWithModel.bindToModel(colors, SingleSelectionListBean.PROPERTY_VALUES, String.class, null, colors,
				SingleSelectionListBean.PROPERTY_SELECTION);

		comboRidgetWithoutModel = (IComboRidget) getRidget("comboBoxWithoutModel"); //$NON-NLS-1$

		updateAllRidgetsFromModel = (IActionRidget) getRidget("updateAllRidgetsFromModel"); //$NON-NLS-1$
		updateAllRidgetsFromModel.addListener(new IActionListener() {

			public void callback() {
				updateAllRidgetsFromModel();
			}
		});

		final ISingleChoiceRidget compositeNumberModel = (ISingleChoiceRidget) getRidget("compositeNumberModel"); //$NON-NLS-1$

		bindComboToModel = (IActionRidget) getRidget("bindComboToModel"); //$NON-NLS-1$
		bindComboToModel.addListener(new IActionListener() {

			public void callback() {
				SingleSelectionListBean colors = new SingleSelectionListBean(new Object[] {
						"white", "black", "red", "blue", "green", "brown", "yellow" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$
				colors.setSelection("red"); //$NON-NLS-1$
				comboRidgetWithoutModel.bindToModel(colors, SingleSelectionListBean.PROPERTY_VALUES, String.class,
						null, colors, SingleSelectionListBean.PROPERTY_SELECTION);
			}
		});

		bindChoiceToModel = (IActionRidget) getRidget("bindChoiceToModel"); //$NON-NLS-1$
		bindChoiceToModel.addListener(new IActionListener() {

			public void callback() {
				SingleSelectionListBean numbers = new SingleSelectionListBean(new Object[] {
						"choice 1", "choice 2", "choice 3" }); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				numbers.setSelection("choice 1"); //$NON-NLS-1$
				compositeNumberModel.bindToModel(numbers, SingleSelectionListBean.PROPERTY_VALUES, numbers,
						SingleSelectionListBean.PROPERTY_SELECTION);
			}
		});

	}

}