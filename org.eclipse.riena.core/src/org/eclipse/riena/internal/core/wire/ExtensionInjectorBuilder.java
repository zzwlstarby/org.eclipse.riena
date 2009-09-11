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
package org.eclipse.riena.internal.core.wire;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.eclipse.core.runtime.Assert;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.injector.extension.ExtensionDescriptor;
import org.eclipse.riena.core.injector.extension.ExtensionInjector;
import org.eclipse.riena.core.util.Nop;
import org.eclipse.riena.core.util.StringUtils;
import org.eclipse.riena.core.wire.InjectExtension;

/**
 * Build a {@code ExtensionInjector} for the given bean and method with the
 * {@code InjectExtension} annotation.
 */
public class ExtensionInjectorBuilder {

	private static final String ID = "ID"; //$NON-NLS-1$
	private final Object bean;
	private final Method method;
	private final InjectExtension annotation;

	/**
	 * Create extension injector builder.
	 * 
	 * @param bean
	 * @param method
	 */
	public ExtensionInjectorBuilder(final Object bean, final Method method) {
		Assert.isLegal(bean != null, "bean must not be null"); //$NON-NLS-1$
		Assert.isLegal(method != null, "method must not be null"); //$NON-NLS-1$
		this.bean = bean;
		this.method = method;
		this.annotation = method.getAnnotation(InjectExtension.class);
		Assert.isLegal(annotation != null, "annotation must not be null"); //$NON-NLS-1$
	}

	/**
	 * Build the {@code ExtensionInjector}
	 * 
	 * @return
	 */
	public ExtensionInjector build() {
		Class<?>[] types = method.getParameterTypes();
		Assert.isLegal(types.length == 1, "only one parameter allowed on �update� method"); //$NON-NLS-1$
		ExtensionDescriptor descriptor = Inject.extension(getExtensionPointId(annotation.id(), types[0]));
		descriptor = descriptor.expectingMinMax(annotation.min(), annotation.max());
		if (types[0].isArray()) {
			descriptor = descriptor.expectingMinMax(annotation.min(), annotation.max()).useType(
					types[0].getComponentType());
		} else {
			// check default values - if no default values given take them (although they might be wrong) otherwise choose useful values  
			int min = annotation.min() == 0 ? 0 : annotation.min();
			int max = annotation.max() == Integer.MAX_VALUE ? 1 : annotation.max();
			descriptor = descriptor.expectingMinMax(min, max).useType(types[0]);
		}
		if (annotation.heterogeneous()) {
			descriptor = descriptor.heterogeneous();
		}
		ExtensionInjector injector = descriptor.into(bean);
		if (annotation.doNotReplaceSymbols()) {
			injector = injector.doNotReplaceSymbols();
		}
		if (annotation.specific()) {
			injector = injector.specific();
		}
		return injector.update(method.getName());
	}

	private String getExtensionPointId(String id, Class<?> parameterType) {
		if (StringUtils.isGiven(id)) {
			return id;
		}
		Class<?> extensionInterfaceType = parameterType.isArray() ? parameterType.getComponentType() : parameterType;
		try {
			Field idField = extensionInterfaceType.getField(ID);
			Object value = idField.get(null);
			if (value instanceof String) {
				return (String) value;
			}
		} catch (Exception e) {
			Nop.reason("Fall throuh! Will be handled by the ExtensionDescriptors input validation."); //$NON-NLS-1$
		}
		return null;
	}
}
