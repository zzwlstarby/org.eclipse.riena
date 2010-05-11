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
package org.eclipse.riena.core.injector.extension;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;

import org.eclipse.riena.core.injector.Inject;
import org.eclipse.riena.core.util.VariableManagerUtil;
import org.eclipse.riena.internal.core.test.ExtensionRegistryAnalyzer;
import org.eclipse.riena.internal.core.test.RienaTestCase;
import org.eclipse.riena.internal.core.test.collect.NonUITestCase;
import org.eclipse.riena.internal.tests.Activator;

/**
 * Test the {@code ExtensionInjector}.
 */
@NonUITestCase
public class ExtensionInjectorTest extends RienaTestCase {

	public void testConstructorConstraints() {
		printTestName();
		try {
			Inject.extension(null);
			fail("Exception expected");
		} catch (RuntimeException expected) {
			ok("Exception expected");
		}
	}

	public void testUseTypeConstraints() {
		printTestName();
		try {
			Inject.extension("id").useType(null);
			fail("Exception expected");
		} catch (RuntimeException expected) {
			ok("Exception expected");
		}
		try {
			Inject.extension("id").useType(IData.class).useType(IData.class);
			fail("Exception expected");
		} catch (RuntimeException expected) {
			ok("Exception expected");
		}
		try {
			Inject.extension("id").useType(String.class);
			fail("Exception expected");
		} catch (RuntimeException expected) {
			ok("Exception expected");
		}
	}

	public void testExpectingConstraints() {
		printTestName();
		try {
			Inject.extension("id").expectingMinMax(2, 1);
			fail("Exception expected");
		} catch (RuntimeException expected) {
			ok("Exception expected");
		}
		try {
			Inject.extension("id").expectingMinMax(-1, 0);
			fail("Exception expected");
		} catch (RuntimeException expected) {
			ok("Exception expected");
		}
		try {
			Inject.extension("id").expectingMinMax(0, 0);
			fail("Exception expected");
		} catch (RuntimeException expected) {
			ok("Exception expected");
		}
	}

	public void testInjectIntoConstraints() {
		printTestName();
		try {
			Inject.extension("id").into(null);
			fail("Exception expected");
		} catch (RuntimeException expected) {
			ok("Exception expected");
		}
	}

	public void testWithKnownTypeAndMultipleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
					.andStart(getContext());
			try {
				assertEquals(3, target.getData().length);
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtension("core.test.extpoint.id2");
			removeExtension("core.test.extpoint.id3");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testWithUnknownTypeAndMultipleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").into(target).andStart(getContext());
			try {
				assertEquals(3, target.getData().length);
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtension("core.test.extpoint.id2");
			removeExtension("core.test.extpoint.id3");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testWithKnownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class)
					.expectingExactly(1).into(target).update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertTrue(target.getData().getValue().contains("And Now for Something Completely Different!"));
				assertTrue(target.getData().getRequired());
				assertTrue(target.getData().isRequired());
				assertEquals("test1", target.getData().getText());
				assertEquals(String.class, target.getData().createObjectType().getClass());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testNonCachingOfCreateWithKnownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class)
					.expectingExactly(1).into(target).update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				String created1 = (String) target.getData().createObjectType();
				String created2 = (String) target.getData().createObjectType();
				assertNotSame(created1, created2);
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testWithUnknownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertFalse(target.getData().getRequired());
				assertFalse(target.getData().isRequired());
				assertEquals("test2", target.getData().getText());
				assertEquals(HashMap.class, target.getData().createObjectType().getClass());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id2");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testCoerceWithUnknownTypeAndSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext4.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertTrue(target.getData().getRequired());
				assertEquals("test4", target.getData().getText());
				assertEquals(ArrayList.class, target.getData().createObjectType().getClass());
				assertEquals(0x2A, target.getData().getJustAByte());
				assertEquals(2.7182818284590452d, target.getData().getDoubleNumber());
				assertEquals(3.14159f, target.getData().getFloatNumber());
				assertEquals(123, target.getData().getIntegerNumber());
				assertEquals(1234567890L, target.getData().getLongNumber());
				assertEquals(1, target.getData().getShortNumber());
				assertEquals('#', target.getData().getDelimCharacter());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id4");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	/**
	 * This test is brittle because it deals with asynchronous events.
	 * 
	 * @throws InterruptedException
	 */
	// TODO enable when 302679 is addressed
	public void XXX_testTrackingWithKnownTypeAndMultipleData() throws InterruptedException {
		System.out.println("ExtensionInjectorTest.testTrackingWithKnownTypeAndMultipleData()");
		printTestName();
		final int sleepInMs = 500;
		ExtensionRegistryAnalyzer.dumpRegistry("core.test.extpoint");
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml", sleepInMs);
		ExtensionRegistryAnalyzer.dumpRegistry("core.test.extpoint");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			target.setTrace(true);
			//			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
			//					.andStart(getContext());
			ExtensionInjector injector = null;
			try {
				//				assertEquals(0, target.getData().length);
				//				Set<String> before = ExtensionRegistryAnalyzer.getRegistryPaths(null);
				addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml", sleepInMs);
				ExtensionRegistryAnalyzer.dumpRegistry("core.test.extpoint");
				injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target).andStart(
						getContext());
				//				Set<String> after = ExtensionRegistryAnalyzer.getRegistryPaths(null);
				//				System.out.println("SymmetricDiff: " + ExtensionRegistryAnalyzer.symmetricDiff(before, after));
				// SymmetricDiff: [core.test.extpoint: uid=core.test.extpoint.id1 bundle=org.eclipse.riena.tests <test required=true objectType=java.lang.String text=test1/>]
				// update: [Lorg.eclipse.riena.core.injector.extension.IData;@1531a15 - length= 0

				try {
					// this fails here - target.getData().length is 0.
					// From console.log:
					// update: [Lorg.eclipse.riena.core.injector.extension.IData;@1892bd8 - length= 0
					// update: [Lorg.eclipse.riena.core.injector.extension.IData;@5b784b - length= 0
					assertEquals(1, target.getData().length);
					addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml", sleepInMs);
					ExtensionRegistryAnalyzer.dumpRegistry("core.test.extpoint");
					try {
						assertEquals(2, target.getData().length);
						addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml", sleepInMs);
						ExtensionRegistryAnalyzer.dumpRegistry("core.test.extpoint");
						try {
							assertEquals(3, target.getData().length);
						} finally {
							removeExtension("core.test.extpoint.id3", sleepInMs);
						}
						assertEquals(2, target.getData().length);
					} finally {
						removeExtension("core.test.extpoint.id2", sleepInMs);
					}
					assertEquals(1, target.getData().length);
				} finally {
					removeExtension("core.test.extpoint.id1", sleepInMs);
				}
				assertEquals(0, target.getData().length);
			} finally {
				injector.stop();
			}
		} finally {
			removeExtensionPoint("core.test.extpoint");
			ExtensionRegistryAnalyzer.dumpRegistry("core.test.extpoint");
		}
	}

	public void testModifyWithUnknownTypeAndSingleData() throws CoreException {
		printTestName();
		VariableManagerUtil.addVariable("true", "true");

		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext5.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertTrue(target.getData().getRequired());
				assertEquals("test5", target.getData().getText());
				assertEquals(String.class, target.getData().createObjectType().getClass());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id5");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testWithUnknownTypeAndSingleDataAndOneNestedSingleElementAndTwoNestedMultipleElements() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext6.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertFalse(target.getData().getRequired());
				assertFalse(target.getData().isRequired());
				assertEquals("test6", target.getData().getText());
				assertEquals(String.class, target.getData().createObjectType().getClass());

				IData2 data2 = target.getData().getNews();
				assertNotNull(data2);
				assertEquals("rmation", data2.getInfo());

				IData3[] data3 = target.getData().getMoreData();
				assertNotNull(data3);
				assertEquals(2, data3.length);
				assertEquals("more-one", data3[0].getId());
				assertEquals("Hugo", data3[0].getName());
				assertEquals("more-two", data3[1].getId());
				assertEquals("Hella", data3[1].getName());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id6");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testWithUnknownTypeDefaultValues() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext8.xml");
		try {
			ConfigurableThingSingleData8 target = new ConfigurableThingSingleData8();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				// required is not defined but the default says that it should be true
				assertTrue(target.getData().isRequired());
				// text is defined with a default, however is should return not the default 
				assertEquals("not empty", target.getData().getText());
				// moreText is not defined and no default is defined so null should be returned
				assertNull(target.getData().getMoreText());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id8");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testCachingWithUnknownTypeAndSingleDataAndOneNestedSingleElementAndTwoNestedMultipleElements() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext6.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertFalse(target.getData().getRequired());
				assertFalse(target.getData().isRequired());
				assertEquals("test6", target.getData().getText());
				assertEquals(String.class, target.getData().createObjectType().getClass());

				IData2 data21 = target.getData().getNews();
				IData2 data22 = target.getData().getNews();
				assertSame(data21, data22);

				IData3[] data31 = target.getData().getMoreData();
				IData3[] data32 = target.getData().getMoreData();
				assertSame(data31, data32);
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id6");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testBug240766WithMultipleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1-sub.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(ISubData.class).into(target)
					.andStart(getContext());
			try {
				assertEquals(3, target.getData().length);
				assertTrue(ISubData.class.isInstance(target.getData()[0]));
				assertEquals("This check relies on the order that extensions are added to the extension registry!",
						"SubSub", ((ISubData) target.getData()[0]).getSubText());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtension("core.test.extpoint.id2");
			removeExtension("core.test.extpoint.id3");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testBug240766WithSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1-sub.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(ISubData.class)
					.expectingExactly(1).into(target).update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertEquals("Dynamic proxy for " + ISubData.class.getName()
						+ ":subText=SubSub,required=true,objectType=java.lang.String,text=test1", target.getData()
						.toString());
				assertTrue(ISubData.class.isInstance(target.getData()));
				assertEquals("SubSub", ((ISubData) target.getData()).getSubText());
				assertTrue(target.getData().getValue().contains("And Now for Something Completely Different!"));
				assertTrue(target.getData().getRequired());
				assertTrue(target.getData().isRequired());
				assertEquals("test1", target.getData().getText());
				assertEquals(String.class, target.getData().createObjectType().getClass());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testBug259478WithSingleData() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext259478.xml");
		try {
			ConfigurableThingSingleData target = new ConfigurableThingSingleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class)
					.expectingExactly(1).into(target).update("configure").andStart(getContext());
			try {
				assertNotNull(target.getData());
				Object obj = target.getData().createObjectType();
				assertTrue(obj instanceof Thing259478);
				Map<String, String> properties = ((Thing259478) obj).properties;
				assertEquals("1", properties.get("eins"));
				assertEquals("2", properties.get("zwei"));
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id8");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testEqualsAndHashCode() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
					.andStart(getContext());
			try {
				assertEquals(2, target.getData().length);
				assertEquals(target.getData()[0], target.getData()[0]);
				assertEquals(target.getData()[1], target.getData()[1]);
				assertFalse(target.getData()[0].equals(target.getData()[1]));
				assertFalse(target.getData()[0].equals("no"));
				assertTrue(
						"This test is based on the fact that the hashCode for IConfigurationElement is based on a handle which is a different int for every element.",
						target.getData()[0].hashCode() != target.getData()[1].hashCode());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtension("core.test.extpoint.id2");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testGetContributor() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
					.andStart(getContext());
			try {
				assertEquals(1, target.getData().length);
				assertEquals(Activator.getDefault().getBundle(), target.getData()[0].getContributingBundle());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testGetConfigurationElement() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
					.andStart(getContext());
			try {
				assertEquals(1, target.getData().length);
				IConfigurationElement conf = target.getData()[0].getConfigurationElement();
				assertEquals("test1", conf.getAttribute("text"));
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testGetClassType() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext7.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
					.andStart(getContext());
			try {
				assertEquals(1, target.getData().length);
				assertEquals(LazyThing.class, target.getData()[0].getLazyThingType());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id7");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testLazyCreate() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext7.xml");
		try {
			ConfigurableThingMultipleData target = new ConfigurableThingMultipleData();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").useType(IData.class).into(target)
					.andStart(getContext());
			try {
				assertEquals(1, target.getData().length);
				assertFalse(LazyThing.instantiated);
				ILazyThing lazyThing = target.getData()[0].createLazyThing();
				assertFalse(LazyThing.instantiated);
				lazyThing.doSomething();
				assertTrue(LazyThing.instantiated);
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id7");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testWithUnknownTypeAndMultipleDataSpecific() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext1.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext2.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext3.xml");
		try {
			ConfigurableThingMultipleDataSpecific target = new ConfigurableThingMultipleDataSpecific();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").into(target).specific().andStart(
					getContext());
			try {
				assertEquals(3, target.getExtData().length);
				for (IExtData data : target.getExtData()) {
					if (data.getTest()[0].getText().equals("test1")) {
						assertEquals("java.lang.String", data.getTest()[0].createObjectType().getClass().getName());
					} else if (data.getTest()[0].getText().equals("test2")) {
						assertEquals("java.util.HashMap", data.getTest()[0].createObjectType().getClass().getName());
					} else if (data.getTest()[0].getText().equals("test3")) {
						assertEquals("java.util.ArrayList", data.getTest()[0].createObjectType().getClass().getName());
					} else {
						fail("Argh!");
					}
				}
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id1");
			removeExtension("core.test.extpoint.id2");
			removeExtension("core.test.extpoint.id3");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testWithUnknownTypeAndMultipleDataSpecific2() {
		printTestName();
		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext-a.xml");
		try {
			ConfigurableThingMultipleDataSpecific target = new ConfigurableThingMultipleDataSpecific();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").into(target).specific().andStart(
					getContext());
			try {
				assertEquals(1, target.getExtData().length);
				for (IExtData extData : target.getExtData()) {
					assertEquals(2, extData.getTest().length);
					for (IData data : extData.getTest()) {
						if (data.getText().equals("test-a1")) {
							assertEquals("java.lang.String", data.createObjectType().getClass().getName());
						} else if (data.getText().equals("test-a2")) {
							assertEquals("java.lang.StringBuffer", data.createObjectType().getClass().getName());
						} else {
							fail("Argh!");
						}
					}
				}
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id-a");
			removeExtensionPoint("core.test.extpoint");
		}
	}

	public void testModifyGlobalOn() throws CoreException {
		printTestName();
		VariableManagerUtil.addVariable("value", "true");
		VariableManagerUtil.addVariable("text", "Hallo!");

		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext-modify.xml");
		try {
			ConfigurableThingModify target = new ConfigurableThingModify();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertTrue(target.getData().isRequired());
				assertEquals("Hallo!", target.getData().getText());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id.modify");
			removeExtensionPoint("core.test.extpoint");
			VariableManagerUtil.removeVariable("value");
			VariableManagerUtil.removeVariable("text");
		}
	}

	public void testModifyGlobalOff() throws CoreException {
		printTestName();
		VariableManagerUtil.addVariable("value", "true");
		VariableManagerUtil.addVariable("text", "Hallo!");

		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext-modify.xml");
		try {
			ConfigurableThingModify target = new ConfigurableThingModify();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.doNotReplaceSymbols().andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertEquals("${value}", target.getData().getRequired());
				assertEquals("${text}", target.getData().getText());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id.modify");
			removeExtensionPoint("core.test.extpoint");
			VariableManagerUtil.removeVariable("value");
			VariableManagerUtil.removeVariable("text");
		}
	}

	public void testModifyInterfaceOff() throws CoreException {
		printTestName();
		VariableManagerUtil.addVariable("value", "true");
		VariableManagerUtil.addVariable("text", "Hallo!");

		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext-modify.xml");
		try {
			ConfigurableThingModifyInterfaceOff target = new ConfigurableThingModifyInterfaceOff();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertEquals("${value}", target.getData().getRequired());
				assertEquals("${text}", target.getData().getText());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id.modify");
			removeExtensionPoint("core.test.extpoint");
			VariableManagerUtil.removeVariable("value");
			VariableManagerUtil.removeVariable("text");
		}
	}

	public void testModifyMethodOff() throws CoreException {
		printTestName();
		VariableManagerUtil.addVariable("value", "true");
		VariableManagerUtil.addVariable("text", "Hallo!");

		addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
		addPluginXml(ExtensionInjectorTest.class, "plugin_ext-modify.xml");
		try {
			ConfigurableThingModifyMethodOff target = new ConfigurableThingModifyMethodOff();
			ExtensionInjector injector = Inject.extension("core.test.extpoint").expectingExactly(1).into(target)
					.andStart(getContext());
			try {
				assertNotNull(target.getData());
				assertEquals("true", target.getData().getRequired());
				assertEquals("${text}", target.getData().getText());
			} finally {
				injector.stop();
			}
		} finally {
			removeExtension("core.test.extpoint.id.modify");
			removeExtensionPoint("core.test.extpoint");
			VariableManagerUtil.removeVariable("value");
			VariableManagerUtil.removeVariable("text");
		}
	}

	// public void testMasses() {
	// printTestName();
	// addPluginXml(ExtensionInjectorTest.class, "plugin.xml");
	// final int A_LOT = 1500;
	// StopWatch watch = new StopWatch("MassTest (" + A_LOT + ")");
	// watch.start();
	// for (int i = 0; i < A_LOT; i++) {
	// addPluginXml(ExtensionInjectorTest.class, "plugin_ext.xml");
	// }
	// watch.elapsed("add").reset();
	// ConfigurableThingMultipleData target = new
	// ConfigurableThingMultipleData();
	// ExtensionInjector injector =
	// Inject.extension("core.test.extpoint").into(target
	// ).andStart(getContext());
	// watch.elapsed("inject").reset();
	// System.out.println("Injected: " + target.getData().length);
	// for (IData data : target.getData()) {
	// data.getText();
	// }
	// watch.elapsed("cycle for getText()").reset();
	// }

}