/*******************************************************************************
 * Copyright (c) 2007 compeople AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    compeople AG - initial API and implementation
 *******************************************************************************/
package org.eclipse.riena.internal.communication.factory.hessian;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.security.Principal;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

import org.eclipse.equinox.log.Logger;
import org.eclipse.riena.communication.core.hooks.ICallMessageContext;
import org.eclipse.riena.communication.core.hooks.ICallMessageContextAccessor;
import org.eclipse.riena.communication.core.publisher.RSDPublisherProperties;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.log.LogService;

import com.caucho.hessian.client.HessianProxyFactory;
import com.caucho.hessian.io.AbstractSerializerFactory;
import com.caucho.hessian.io.Deserializer;
import com.caucho.hessian.io.HessianProtocolException;
import com.caucho.hessian.io.JavaDeserializer;
import com.caucho.hessian.io.Serializer;
import com.caucho.hessian.io.SerializerFactory;

public class RienaHessianProxyFactory extends HessianProxyFactory implements ManagedService {

	private ICallMessageContextAccessor mca;
	private static ThreadLocal<HttpURLConnection> connections = new ThreadLocal<HttpURLConnection>();
	private URL url;
	private final static Logger LOGGER = Activator.getDefault().getLogger(RienaHessianProxyFactory.class.getName());

	public RienaHessianProxyFactory() {
		super();
		getSerializerFactory().addFactory(new AbstractSerializerFactory() {
			@Override
			public Deserializer getDeserializer(Class cl) throws HessianProtocolException {
				if (cl.isInterface() && (!cl.getPackage().getName().startsWith("java") || cl == Principal.class)) {
					return new JavaDeserializer(cl);
				}
				return null;
			}

			@Override
			public Serializer getSerializer(Class cl) throws HessianProtocolException {
				return null;
			}
		});
	}

	@Override
	protected URLConnection openConnection(URL url) throws IOException {
		URLConnection connection;
		if (this.url != null) {
			connection = super.openConnection(this.url);
		} else {
			connection = super.openConnection(url);
		}
		ICallMessageContext mc = mca.getMessageContext();
		Map<String, List<String>> headers = mc.listRequestHeaders();
		if (headers != null) {
			for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
				// System.out.println("size:" + headers.get(hName).size());
				for (String hValue : entry.getValue()) {
					connection.addRequestProperty(entry.getKey(), hValue);
					// System.out.println(">>>" + hName + ":" + hValue);
				}
			}
		}
		connections.set((HttpURLConnection) connection);
		return connection;
	}

	@Override
	public SerializerFactory getSerializerFactory() {
		SerializerFactory serializerFactory = super.getSerializerFactory();
		serializerFactory.setAllowNonSerializable(true);
		return serializerFactory;
	}

	public void setCallMessageContextAccessor(ICallMessageContextAccessor mca) {
		this.mca = mca;
	}

	public static HttpURLConnection getHttpURLConnection() {
		return connections.get();
	}

	@SuppressWarnings("unchecked")
	public void updated(Dictionary properties) throws ConfigurationException {
		if (properties == null)
			return;
		String urlStr = (String) properties.get(RSDPublisherProperties.PROP_REMOTE_PATH);
		if (urlStr == null)
			return;

		try {
			this.url = new URL(urlStr);
		} catch (MalformedURLException e) {
			LOGGER.log(LogService.LOG_ERROR, "invalid url " + urlStr, e);
		}

	}

}
