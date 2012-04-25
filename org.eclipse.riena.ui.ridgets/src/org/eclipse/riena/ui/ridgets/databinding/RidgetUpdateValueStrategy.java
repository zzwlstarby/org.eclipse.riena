/*******************************************************************************
 * Copyright (c) 2007, 2012 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.ui.ridgets.databinding;

import java.util.GregorianCalendar;

import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.core.runtime.IStatus;

import org.eclipse.riena.ui.ridgets.ValueBindingSupport;
import org.eclipse.riena.ui.ridgets.validation.ValidatorCollection;

/**
 * 
 */
public class RidgetUpdateValueStrategy extends UpdateValueStrategy {

	private ValidatorCollection afterModelUpdateValidators;
	private final ValueBindingSupport valueBindingSupport;

	/**
	 * @param valueBindingSupport
	 * @since 4.0
	 */
	public RidgetUpdateValueStrategy(final ValueBindingSupport valueBindingSupport) {
		super();
		this.valueBindingSupport = valueBindingSupport;
	}

	/**
	 * @param valueBindingSupport
	 * @param updatePolicy
	 * @since 4.0
	 */
	public RidgetUpdateValueStrategy(final ValueBindingSupport valueBindingSupport, final int updatePolicy) {
		super(updatePolicy);
		this.valueBindingSupport = valueBindingSupport;
	}

	/**
	 * @param valueBindingSupport
	 * @param provideDefaults
	 * @param updatePolicy
	 * @since 4.0
	 */
	public RidgetUpdateValueStrategy(final ValueBindingSupport valueBindingSupport, final boolean provideDefaults, final int updatePolicy) {
		super(provideDefaults, updatePolicy);
		this.valueBindingSupport = valueBindingSupport;
	}

	/**
	 * @see org.eclipse.core.databinding.UpdateStrategy#createConverter(java.lang.Object, java.lang.Object)
	 */
	@Override
	protected IConverter createConverter(final Object fromType, final Object toType) {

		if (fromType == String.class) {
			if (toType == Double.TYPE) {
				return StringToNumberAllowingNullConverter.toPrimitiveDouble();
			} else if (toType == Float.TYPE) {
				return StringToNumberAllowingNullConverter.toPrimitiveFloat();
			} else if (toType == Long.TYPE) {
				return StringToNumberAllowingNullConverter.toPrimitiveLong();
			} else if (toType == Integer.TYPE) {
				return StringToNumberAllowingNullConverter.toPrimitiveInteger();
			} else if (toType == GregorianCalendar.class) {
				return new StringToGregorianCalendarConverter();
			}
		}
		if (fromType == GregorianCalendar.class) {
			if (toType == String.class) {
				return new GregorianCalendarToStringConverter();
			}
		}

		return super.createConverter(fromType, toType);
	}

	/**
	 * @param afterModelUpdateValidators
	 * @since 4.0
	 */
	public void setAfterModelUpdateValidator(final ValidatorCollection afterModelUpdateValidators) {
		this.afterModelUpdateValidators = afterModelUpdateValidators;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.databinding.UpdateValueStrategy#doSet(org.eclipse.core.databinding.observable.value.IObservableValue, java.lang.Object)
	 */
	@Override
	protected IStatus doSet(final IObservableValue destination, final Object convertedValue) {
		final IStatus status = super.doSet(destination, convertedValue);

		if (status.isOK() && afterModelUpdateValidators != null) {
			final IObservableValue targetOV = valueBindingSupport.getTargetOV();
			return afterModelUpdateValidators.validate(targetOV.getValue());
		}
		return status;
	}
}
